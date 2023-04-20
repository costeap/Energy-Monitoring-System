package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.Device;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class AdminDTO extends RepresentationModel<AdminDTO> {
    private UUID id;
    private String name;
    private String username;
    private String password;


    public AdminDTO(UUID id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
}