package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class ClientDTO extends RepresentationModel<ClientDTO> {
    private UUID id;
    private String name;
    private String username;
    private String password;

    public ClientDTO(UUID id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
}