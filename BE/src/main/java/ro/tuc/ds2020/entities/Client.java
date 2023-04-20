package ro.tuc.ds2020.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ro.tuc.ds2020.controllers.UserController;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "CLIENT")
public class Client extends UserEntity {

    @Builder
    public Client(UUID id, String name, String username, String password) {
        super(id, name, username, password);
    }

    public Client() {

    }

    @Override
    public String toString() {
        return "Client{" + super.toString() + '}';
    }
}
