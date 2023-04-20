package ro.tuc.ds2020.controllers;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ro.tuc.ds2020.Utils.NotificationEndpoints;
import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.services.ConsumptionService;
import ro.tuc.ds2020.services.DeviceService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class RabbitMQController implements MessageListener {

    private final DeviceService deviceService;

    private final ConsumptionService consumptionService;

    private final DeviceRepository deviceRepository;

    private final SimpMessagingTemplate template;

    List<Float> measurements = new ArrayList<Float>();

    public RabbitMQController(DeviceService deviceService, ConsumptionService consumptionService, DeviceRepository deviceRepository, SimpMessagingTemplate template) {
        this.deviceService = deviceService;
        this.consumptionService = consumptionService;
        this.deviceRepository = deviceRepository;
        this.template = template;
    }

    public void onMessage(Message message) {
        Object object = SerializationUtils.deserialize(message.getBody());
        Measurement measurement = (Measurement) object;
        //System.out.println("Aici: " + measurement.toString());
        //System.out.println("Consuming Message - " + object.toString());
        measurements.add(measurement.getMeasurementValue());
        System.out.println(measurements.toString());

        UUID device_id = (deviceRepository.findDeviceById(measurement.getDevice_id())).getId();
        Device device = deviceRepository.findDeviceById(measurement.getDevice_id());
        LocalDateTime localDateTime = (measurement.getTimestamp()).toLocalDateTime();
        int day = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();

        List<ConsumptionDTO> consumptionDTOS = consumptionService.findAll();

        float suma = 0;
        for (ConsumptionDTO consumptionDTO : consumptionDTOS) {
            LocalDateTime localDateTime1 = (consumptionDTO.getTimestamp()).toLocalDateTime();
            if (localDateTime1.getDayOfMonth() == day && localDateTime1.getHour() == hour && (consumptionDTO.getDeviceID().toString()).equals(device_id.toString())) {
                suma = suma + Float.parseFloat(consumptionDTO.getEnergyConsumption());
            }
        }

        float diff = 0;
        if (measurements.size() == 2) {
            diff = measurements.get(1) - measurements.get(0);
            if (suma + diff > Float.parseFloat(device.getMaximumHourlyEnergyConsumption())) {
                System.out.println("Se depaseste!");
                System.out.println(device_id.toString());
                //device.getClient().getId().toString()
                this.template.convertAndSend(NotificationEndpoints.MAXIMUM_HOURLY_CONSUMPTION_EXCEEDED, device_id.toString());
            } else {
                System.out.println("E ok");
                if (diff > 0) {
                    ConsumptionDTO consumptionDTO = new ConsumptionDTO(UUID.randomUUID(), new Timestamp(System.currentTimeMillis()), Float.toString(diff), device_id.toString());
                    consumptionService.insert(consumptionDTO);
                }
            }
            measurements.clear();
            measurements.add(measurement.getMeasurementValue());
        }

    }
}