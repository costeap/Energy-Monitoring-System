package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class AdminDTOforUpdate extends RepresentationModel<AdminDTOforUpdate> {
    private String name;
    private String password;

    public AdminDTOforUpdate(String name, String password) {
        this.name = name;
        this.password = password;
    }
}