package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class ClientDTOforUpdate extends RepresentationModel<ClientDTOforUpdate> {
    private String name;
    private String password;

    public ClientDTOforUpdate(String name, String password) {
        this.name = name;
        this.password = password;
    }
}