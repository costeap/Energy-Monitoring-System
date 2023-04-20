package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findById(UUID id);

    void deleteUserById(UUID id);

    UserEntity findFirstByUsernameAndPassword(String username, String parola);

    UserEntity findFirstByUsername(String username);

    UserEntity findByUsername(String username);
}
