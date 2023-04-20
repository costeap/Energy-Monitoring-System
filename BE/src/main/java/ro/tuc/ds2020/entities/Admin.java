package ro.tuc.ds2020.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Admin extends UserEntity {

    @Builder
    public Admin(UUID id, String name, String username, String password) {
        super(id, name, username, password);
    }

    public Admin() {

    }

    @Override
    public String toString() {
        return "Admin{" + super.toString() + '}';
    }
}
