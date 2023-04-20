package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.UserEntity;

public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTO(UserEntity user) {
        return new UserDTO(user.getUsername(), user.getPassword());
    }

    /*public static UserEntity toEntity(UserDTO userDTO) {
        return new UserEntity(userDTO.getUsername(), userDTO.getPassword());
    }*/
}
