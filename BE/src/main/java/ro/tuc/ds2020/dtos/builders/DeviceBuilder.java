package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(device.getId(), device.getDescription(), device.getAddress(), device.getMaximumHourlyEnergyConsumption(), device.getClient());
    }

    /*public static DeviceDTOforUpdate toDeviceDTOforUpdate(Device device) {
        return new DeviceDTOforUpdate(device.getDescription(), device.getAddress(), device.getMaximumHourlyEnergyConsumption());
    }*/

    public static Device toEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getId(), deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getMaximumHourlyEnergyConsumption(), deviceDTO.getClient());
    }

    public static DeviceDTO mapToDTO(Device device) {
        return DeviceDTO.builder()
                .id(device.getId())
                .description(device.getDescription())
                .address(device.getAddress())
                .maximumHourlyEnergyConsumption(device.getMaximumHourlyEnergyConsumption())
                .client(device.getClient())
                .build();
    }
}
