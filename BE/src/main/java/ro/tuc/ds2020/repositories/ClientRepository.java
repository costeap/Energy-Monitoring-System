package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findById(UUID id);

    Optional<Client> findByUsername(String username);

    Client findClientById(UUID id);

    void deleteClientById(UUID id);
}