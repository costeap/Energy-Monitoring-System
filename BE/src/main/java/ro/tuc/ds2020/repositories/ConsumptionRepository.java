package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.Consumption;
import ro.tuc.ds2020.entities.Device;

import java.util.UUID;

public interface ConsumptionRepository extends JpaRepository<Consumption, UUID> {
    Consumption findConsumptionById(UUID id);

    void deleteAllByDevice(Device device);


}
