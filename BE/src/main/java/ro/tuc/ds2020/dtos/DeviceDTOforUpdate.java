package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class DeviceDTOforUpdate extends RepresentationModel<DeviceDTOforUpdate> {
    private String description;
    private String address;
    private String maximumHourlyEnergyConsumption;
    private String clientID;

    public DeviceDTOforUpdate(String description, String address, String maximumHourlyEnergyConsumption, String clientID) {
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.clientID = clientID;
    }
}