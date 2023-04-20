package ro.tuc.ds2020.controllers;

import io.grpc.stub.CallStreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.MessageRequest;
import ro.tuc.ds2020.MessageResponse;
import ro.tuc.ds2020.dtos.MessageDTO;
import ro.tuc.ds2020.dtos.MessageDTOForDelete;
import ro.tuc.ds2020.dtos.builders.MessageBuilder;
import ro.tuc.ds2020.server.Server;
import ro.tuc.ds2020.services.MessageService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/server")
public class ServerController {

    private final Server server;
    private final MessageService messageService;

    @Autowired
    public ServerController(Server server, MessageService messageService) {
        this.server = server;
        this.messageService = messageService;
    }

    @PostMapping()
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageDTO messageDTO) {
        MessageRequest messageRequest = MessageBuilder.toMessageRequest(messageDTO);
        io.grpc.stub.StreamObserver<ro.tuc.ds2020.MessageResponse> responseObserver = new CallStreamObserver<MessageResponse>() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setOnReadyHandler(Runnable runnable) {

            }

            @Override
            public void disableAutoInboundFlowControl() {

            }

            @Override
            public void request(int i) {

            }

            @Override
            public void setMessageCompression(boolean b) {

            }

            @Override
            public void onNext(MessageResponse messageResponse) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        };
        server.sendMessage(messageRequest, responseObserver);
        messageService.insert(messageDTO);
        System.out.println(messageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/del")
    @Transactional
    public ResponseEntity<?> deleteDevice(@Valid @RequestBody MessageDTOForDelete messageDTO) {
        messageService.delete(messageDTO.getMessageSender(), messageDTO.getMessageReceiver());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<List<String>> getMessages(@Valid @RequestBody MessageDTOForDelete messageDTO) {
        List<String> messagesList = messageService.findMessagesByMessageSenderAndMessageReceiver(messageDTO.getMessageSender(), messageDTO.getMessageReceiver());
        return new ResponseEntity<>(messagesList, HttpStatus.OK);
    }

    @PostMapping("/typing")
    public ResponseEntity<List<String>> isTyping(@Valid @RequestBody MessageDTOForDelete messageDTO) {
        server.isTyping(messageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/read")
    public ResponseEntity<List<String>> setMessagesAsRead(@Valid @RequestBody MessageDTOForDelete messageDTO) {
        messageService.markAsRead(messageDTO.getMessageSender(), messageDTO.getMessageReceiver());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
