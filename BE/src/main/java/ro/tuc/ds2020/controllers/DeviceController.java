package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.ClientDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDTOforUpdate;
import ro.tuc.ds2020.services.DeviceService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        for (DeviceDTO dto : dtos) {
            Link userLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("device");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @DeleteMapping("/del/{id}")
    @Transactional
    public ResponseEntity<?> deleteDevice(@PathVariable("id") String id) {
        deviceService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID deviceId) {
        DeviceDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DeviceDTO> updateDevice(@Valid @RequestBody DeviceDTOforUpdate deviceDTOforUpdate, @PathVariable UUID id) {
        DeviceDTO updateDevice = deviceService.updateDevice(deviceDTOforUpdate, id);
        return new ResponseEntity<>(updateDevice, HttpStatus.OK);
    }

    @GetMapping(value = "client/{id}")
    public ResponseEntity<ClientDTO> getClientForDevice(@PathVariable("id") UUID deviceId) {
        ClientDTO dto = deviceService.getClientForDevice(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("clientDevice/{id}")
    public ResponseEntity<List<DeviceDTO>> getDevices(@PathVariable("id") UUID clientId)  {
        List<DeviceDTO> dtos = deviceService.findDevicesForClient(clientId);
        for (DeviceDTO dto : dtos) {
            Link userLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("device");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
