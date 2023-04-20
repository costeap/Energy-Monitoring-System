package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.ClientDTO;
import ro.tuc.ds2020.dtos.ClientDTOforUpdate;
import ro.tuc.ds2020.dtos.builders.ClientBuilder;
import ro.tuc.ds2020.entities.Client;
import ro.tuc.ds2020.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.clientRepository = clientRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public ClientDTO findClientById(UUID id) {
        Optional<Client> prosumerOptional = clientRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Client with id {} was not found in db", id);
            throw new ResourceNotFoundException(Client.class.getSimpleName() + " with id: " + id);
        }
        return ClientBuilder.toClientDTO(prosumerOptional.get());
    }

    public UUID insert(ClientDTO clientDTO) {
        Client client = ClientBuilder.toEntity(clientDTO);
        String encodePassword = bCryptPasswordEncoder.encode(clientDTO.getPassword());
        client.setPassword(encodePassword);
        client = clientRepository.save(client);
        LOGGER.debug("Client with id {} was inserted in db", client.getId());
        return client.getId();
    }

    public void delete(UUID id) {
        clientRepository.deleteClientById(id);
        LOGGER.debug("Client with id {} was deleted from db", id);
    }

    public List<ClientDTO> findClients() {
        List<Client> clientList = clientRepository.findAll();
        return clientList.stream()
                .map(ClientBuilder::toClientDTO)
                .collect(Collectors.toList());
    }

    public ClientDTOforUpdate updateClient(ClientDTOforUpdate clientDTOforUpdate, UUID id) {

        Client client = clientRepository.findClientById(id);
        client.setName(clientDTOforUpdate.getName());
        client.setPassword(clientDTOforUpdate.getPassword());
        Client save = clientRepository.save(client);
        return ClientBuilder.toClientDTOforUpdate(client);
    }
}