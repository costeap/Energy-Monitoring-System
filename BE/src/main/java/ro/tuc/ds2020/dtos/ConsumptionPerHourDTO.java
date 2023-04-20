package ro.tuc.ds2020.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class ConsumptionPerHourDTO {
    private int hour;
    private String energyValue;

    public ConsumptionPerHourDTO(int hour, String energyValue) {
        this.hour = hour;
        this.energyValue = energyValue;
    }

}