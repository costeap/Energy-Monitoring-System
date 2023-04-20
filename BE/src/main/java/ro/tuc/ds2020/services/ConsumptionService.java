package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.Utils.DateConvertor;
import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.dtos.ConsumptionPerHourDTO;
import ro.tuc.ds2020.dtos.DateDTO;
import ro.tuc.ds2020.dtos.builders.ConsumptionBuilder;
import ro.tuc.ds2020.entities.Consumption;
import ro.tuc.ds2020.repositories.ConsumptionRepository;
import ro.tuc.ds2020.repositories.DeviceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConsumptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumptionService.class);
    private final ConsumptionRepository consumptionRepository;
    private final DeviceRepository deviceRepository;
    private LocalDateTime localDateTime;

    @Autowired
    public ConsumptionService(ConsumptionRepository consumptionRepository, DeviceRepository deviceRepository) {
        this.consumptionRepository = consumptionRepository;
        this.deviceRepository = deviceRepository;
    }

    public UUID insert(ConsumptionDTO consumptionDTO) {
        Consumption consumption = new Consumption();
        consumption.setId(consumptionDTO.getId());
        consumption.setTimestamp(consumptionDTO.getTimestamp());
        consumption.setEnergyConsumption(consumptionDTO.getEnergyConsumption());
        consumption.setDevice(deviceRepository.findDeviceById(UUID.fromString(consumptionDTO.getDeviceID())));
        consumption = consumptionRepository.save(consumption);

        return consumption.getId();
    }

    public List<ConsumptionDTO> findAll() {
        List<Consumption> consumptionList = consumptionRepository.findAll();
        return consumptionList.stream()
                .map(ConsumptionBuilder::toConsumptionDTO)
                .collect(Collectors.toList());
    }

    public List<ConsumptionDTO> getConsumptionForOneDevice(UUID deviceId) {
        List<Consumption> consumptionList = consumptionRepository.findAll();
        return consumptionList.stream()
                .map(ConsumptionBuilder::toConsumptionDTO)
                .filter(c -> c.getDeviceID().equals(deviceId.toString()))
                .collect(Collectors.toList());
    }

    public void delete(UUID deviceId) {
        consumptionRepository.deleteAllByDevice(deviceRepository.findDeviceById(deviceId));
    }

    public List<ConsumptionPerHourDTO> getConsumptionPerHours(DateDTO dateDTO, UUID deviceId) {
        List<ConsumptionPerHourDTO> consumptionPerHourDTOList = new ArrayList<>();
        List<Consumption> consumptionList = consumptionRepository.findAll();
        for (Consumption consumption : consumptionList) {
            if ((consumption.getDevice().getId().toString()).equals(deviceId.toString())) {
                LocalDateTime localDateTime = (consumption.getTimestamp()).toLocalDateTime();
                Date date = new Date(dateDTO.getDate());
                LocalDateTime givenDate = DateConvertor.convertToLocalDateTimeViaInstant(date);
                if (localDateTime.getDayOfMonth() == givenDate.getDayOfMonth() && localDateTime.getMonth() == givenDate.getMonth() && localDateTime.getYear() == givenDate.getYear()) {
                    ConsumptionPerHourDTO consumptionPerHourDTO = new ConsumptionPerHourDTO(localDateTime.getHour(), consumption.getEnergyConsumption());
                    consumptionPerHourDTOList.add(consumptionPerHourDTO);
                }
            }
        }
        return consumptionPerHourDTOList;
    }
}
