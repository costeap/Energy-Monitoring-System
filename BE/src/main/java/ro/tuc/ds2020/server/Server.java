package ro.tuc.ds2020.server;

import com.google.protobuf.Timestamp;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.tuc.ds2020.ChatServiceGrpc;
import ro.tuc.ds2020.MessageResponse;
import ro.tuc.ds2020.Utils.NotificationEndpoints;
import ro.tuc.ds2020.dtos.MessageDTOForDelete;
import ro.tuc.ds2020.entities.Admin;
import ro.tuc.ds2020.entities.Client;
import ro.tuc.ds2020.repositories.AdminRepository;
import ro.tuc.ds2020.repositories.ClientRepository;

import java.util.List;

@GRpcService
public class Server extends ChatServiceGrpc.ChatServiceImplBase {

    Logger logger = LoggerFactory.getLogger(Server.class);

    private final SimpMessagingTemplate template;

    private final ClientRepository clientRepository;

    private final AdminRepository adminRepository;

    public Server(SimpMessagingTemplate template, ClientRepository clientRepository, AdminRepository adminRepository) {
        this.template = template;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
    }

    public void sendMessage(ro.tuc.ds2020.MessageRequest request,
                            io.grpc.stub.StreamObserver<ro.tuc.ds2020.MessageResponse> responseObserver) {
        //logger.info("got request" + request.getId());
        long millis = System.currentTimeMillis();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(millis / 1000)
                .setNanos((int) ((millis % 1000) * 1000000)).build();
        MessageResponse messageResponse = MessageResponse.newBuilder().setMessageSender("andreim").setTextMessage("Salut!").setMessageReceiver("costeap").build();
        responseObserver.onNext(messageResponse);
        responseObserver.onCompleted();

        boolean client = false;
        boolean admin = false;
        List<Client> clientList = clientRepository.findAll();
        for (Client c : clientList) {
            if (c.getUsername().equals(request.getMessageSender())) {
                client = true;
            }
        }
        List<Admin> adminList = adminRepository.findAll();
        for (Admin a : adminList) {
            if (a.getUsername().equals(request.getMessageSender())) {
                admin = true;
            }
        }

        if (admin == true) {
            this.template.convertAndSend(NotificationEndpoints.MESSAGE_FROM_ADMIN, "You have a new message from admin " + request.getMessageSender() + ": " + request.getTextMessage());

        } else if (client == true) {
            this.template.convertAndSend(NotificationEndpoints.MESSAGE_FROM_CLIENT, "You have a new message from " + request.getMessageSender() + ": " + request.getTextMessage());

        }
    }

    public void isTyping(MessageDTOForDelete messageDTO) {
        boolean client = false;
        boolean admin = false;
        List<Client> clientList = clientRepository.findAll();
        for (Client c : clientList) {
            if (c.getUsername().equals(messageDTO.getMessageSender())) {
                client = true;
            }
        }
        List<Admin> adminList = adminRepository.findAll();
        for (Admin a : adminList) {
            if (a.getUsername().equals(messageDTO.getMessageSender())) {
                admin = true;
            }
        }

        if (admin == true) {

            this.template.convertAndSend(NotificationEndpoints.ADMIN_IS_TYPING, messageDTO);

        } else if (client == true) {
            this.template.convertAndSend(NotificationEndpoints.CLIENT_IS_TYPING, messageDTO.getMessageSender());

        }

    }

}
