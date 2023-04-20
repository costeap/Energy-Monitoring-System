package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.Client;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class DeviceDTO extends RepresentationModel<DeviceDTO> {
    private UUID id;
    private String description;
    private String address;
    private String maximumHourlyEnergyConsumption;
    private Client client;

    public DeviceDTO(UUID id, String description, String address, String maximumHourlyEnergyConsumption, Client client) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
        this.client = client;
    }
}