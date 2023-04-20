package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Admin;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    Optional<Admin> findById(UUID id);

    Optional<Admin> findByUsername(String username);

    Admin findAdminById(UUID id);

    void deleteAdminById(UUID id);
}