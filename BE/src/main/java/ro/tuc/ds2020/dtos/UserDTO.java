package ro.tuc.ds2020.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends RepresentationModel<UserDTO> {
    private String username;
    private String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
