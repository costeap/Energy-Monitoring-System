package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.UserEntity;
import ro.tuc.ds2020.services.UserService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") UUID userId) {
        UserDTO dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDTO userDTO) {
        String dtype = userService.clientOrAdmin(userDTO);
        UserEntity user = userService.findFirstByUsername(userDTO);
        if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            System.out.println("aici");
            if (dtype.equals("client")) {
                return new ResponseEntity(user, HttpStatus.OK);
            } else if (dtype.equals("administrator")) {
                return new ResponseEntity(user, HttpStatus.valueOf(201));
            }
        } else return new ResponseEntity("Wrong pass", HttpStatus.BAD_REQUEST);

        return new ResponseEntity(user, HttpStatus.valueOf(202));
    }
}
