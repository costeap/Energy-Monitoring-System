package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.ClientDTO;
import ro.tuc.ds2020.dtos.ClientDTOforUpdate;
import ro.tuc.ds2020.services.ClientService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientDTO> dtos = clientService.findClients();
        for (ClientDTO dto : dtos) {
            Link userLink = linkTo(methodOn(ClientController.class)
                    .getClient(dto.getId())).withRel("client");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertClient(@Valid @RequestBody ClientDTO clientDTO) {
        UUID clientID = clientService.insert(clientDTO);
        return new ResponseEntity<>(clientID, HttpStatus.CREATED);
    }

    @DeleteMapping("/del/{id}")
    @Transactional
    public ResponseEntity<?> deleteClient(@PathVariable("id") String id) {
        clientService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable("id") UUID clientId) {
        ClientDTO dto = clientService.findClientById(clientId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ClientDTOforUpdate> updateClient(@Valid @RequestBody ClientDTOforUpdate clientDTOforUpdate, @PathVariable UUID id) {
        ClientDTOforUpdate updateClient = clientService.updateClient(clientDTOforUpdate, id);
        return new ResponseEntity<>(updateClient, HttpStatus.OK);
    }

}
