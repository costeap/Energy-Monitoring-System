package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.ClientDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDTOforUpdate;
import ro.tuc.ds2020.dtos.builders.ClientBuilder;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.ClientRepository;
import ro.tuc.ds2020.repositories.DeviceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, ClientRepository clientRepository) {
        this.deviceRepository = deviceRepository;
        this.clientRepository = clientRepository;
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get());
    }

    public UUID insert(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }

    public void delete(UUID id) {
        deviceRepository.deleteDeviceById(id);
        LOGGER.debug("Device with id {} was deleted from db", id);
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO updateDevice(DeviceDTOforUpdate deviceDTOforUpdate, UUID id) {
        Device device = deviceRepository.findDeviceById(id);
        device.setDescription(deviceDTOforUpdate.getDescription());
        device.setAddress(deviceDTOforUpdate.getAddress());
        device.setMaximumHourlyEnergyConsumption(deviceDTOforUpdate.getMaximumHourlyEnergyConsumption());
        System.out.println("Aici:"+deviceDTOforUpdate.getClientID());
        if ((deviceDTOforUpdate.getClientID()).toString().equals("")) {
            device.setClient(null);
        } else
            device.setClient(clientRepository.findClientById(UUID.fromString(deviceDTOforUpdate.getClientID())));

        Device save = deviceRepository.save(device);
        return DeviceBuilder.mapToDTO(device);
    }

    public ClientDTO getClientForDevice(UUID deviceId) {
        Device device = deviceRepository.findDeviceById(deviceId);
        if (device.getClient() != null)
            return ClientBuilder.toClientDTO(device.getClient());
        else
            return null;
    }

    public List<DeviceDTO> findDevicesForClient(UUID clientId) {
        List<Device> deviceList = deviceRepository.findAll();
        List<Device> devices = new ArrayList<>();
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        for (Device device : deviceList) {
            if (device.getClient() != null) {
                devices.add(device);
            }
        }
        System.out.println(devices);
        for (Device device : devices) {
            if ((device.getClient().getId().toString()).equals(clientId.toString())) {
                deviceDTOS.add(DeviceBuilder.toDeviceDTO(device));
            }
        }

        return deviceDTOS;
    }
}