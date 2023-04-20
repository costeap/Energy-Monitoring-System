package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    Optional<Device> findById(UUID id);

    Device findDeviceById(UUID id);

    void deleteDeviceById(UUID id);
}