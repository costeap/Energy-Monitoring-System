package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.ClientDTO;
import ro.tuc.ds2020.dtos.ClientDTOforUpdate;
import ro.tuc.ds2020.entities.Client;

public class ClientBuilder {

    private ClientBuilder() {
    }

    public static ClientDTO toClientDTO(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getUsername(), client.getPassword());
    }

    public static ClientDTOforUpdate toClientDTOforUpdate(Client client) {
        return new ClientDTOforUpdate(client.getName(), client.getPassword());
    }

    public static Client toEntity(ClientDTO clientDTO) {
        return new Client(clientDTO.getId(), clientDTO.getName(), clientDTO.getUsername(), clientDTO.getPassword());
    }
}
