package ro.tuc.ds2020.dtos;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class DateDTO {
    private Long date;

    public DateDTO(Long date) {
        this.date = date;
    }

}