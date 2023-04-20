package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.Admin;
import ro.tuc.ds2020.entities.Client;
import ro.tuc.ds2020.entities.UserEntity;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findUserById(UUID id) {
        System.out.println(id);
        Optional<UserEntity> prosumerOptional = userRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(UserEntity.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDTO(prosumerOptional.get());
    }

    public void delete(UUID id) {
        userRepository.deleteUserById(id);
        System.out.println("Salut");
        LOGGER.debug("User with id {} was deleted from db", id);
    }

    public List<UserDTO> findUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public String clientOrAdmin(UserDTO userDTO) {
        String dtype = "";
        UserEntity userReturned = userRepository.findByUsername(userDTO.getUsername());
        if (userReturned instanceof Client) {
            dtype = "client";
        } else if (userReturned instanceof Admin) {
            dtype = "administrator";
        }
        return dtype;
    }

    public UserEntity findFirstByUsernameAndPassword(UserDTO userDTO) {

        String dtype = "";
        UserEntity userReturned = userRepository.findFirstByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        if (userReturned instanceof Client) {
            dtype = "client";
        } else if (userReturned instanceof Admin) {
            dtype = "administrator";
        }
        return userReturned;
    }

    public UserEntity findFirstByUsername(UserDTO userDTO) {

        String dtype = "";
        UserEntity userReturned = userRepository.findFirstByUsername(userDTO.getUsername());
        if (userReturned instanceof Client) {
            dtype = "client";
        } else if (userReturned instanceof Admin) {
            dtype = "administrator";
        }
        return userReturned;
    }
}
