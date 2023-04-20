package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.dtos.ConsumptionPerHourDTO;
import ro.tuc.ds2020.dtos.DateDTO;
import ro.tuc.ds2020.services.ConsumptionService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/consumption")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    @Autowired
    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @GetMapping()
    public ResponseEntity<List<ConsumptionDTO>> getAll() {
        return new ResponseEntity<>(consumptionService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertConsumption(@Valid @RequestBody ConsumptionDTO consumptionDTO) {
        UUID userID = consumptionService.insert(consumptionDTO);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<ConsumptionDTO>> getDeviceConsumption(@PathVariable("id") UUID deviceId) {
        return new ResponseEntity<>(consumptionService.getConsumptionForOneDevice(deviceId), HttpStatus.OK);
    }

    @DeleteMapping("/del/{id}")
    @Transactional
    public ResponseEntity<?> deleteConsumption(@PathVariable("id") String id) {
        consumptionService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("perHours/{id}")
    public ResponseEntity<List<ConsumptionPerHourDTO>> getConsumptionPerHours(@PathVariable("id") UUID deviceId, @Valid @RequestBody DateDTO dateDTO) {
        return new ResponseEntity<>(consumptionService.getConsumptionPerHours(dateDTO, deviceId), HttpStatus.OK);
    }


}
