package ro.tuc.ds2020.dtos;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class ConsumptionDTO {
    private UUID id;
    private Timestamp timestamp;
    private String energyConsumption;
    private String deviceID;

    public ConsumptionDTO(UUID id, Timestamp timestamp, String energyConsumption, String deviceID) {
        this.id = id;
        this.timestamp = timestamp;
        this.energyConsumption = energyConsumption;
        this.deviceID = deviceID;
    }
}
