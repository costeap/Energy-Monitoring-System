package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.Utils.NotificationEndpoints;
import ro.tuc.ds2020.dtos.MessageDTO;
import ro.tuc.ds2020.dtos.builders.MessageBuilder;
import ro.tuc.ds2020.entities.Admin;
import ro.tuc.ds2020.entities.Client;
import ro.tuc.ds2020.entities.Message;
import ro.tuc.ds2020.repositories.AdminRepository;
import ro.tuc.ds2020.repositories.ClientRepository;
import ro.tuc.ds2020.repositories.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final SimpMessagingTemplate template;

    @Autowired
    public MessageService(MessageRepository messageRepository, ClientRepository clientRepository, AdminRepository adminRepository, SimpMessagingTemplate template) {
        this.messageRepository = messageRepository;
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
        this.template = template;
    }

    public List<String> findMessagesByMessageSenderAndMessageReceiver(String messageSender, String messageReceiver) {
        //List<Message> messagesList = messageRepository.findMessagesByMessageSenderAndMessageReceiver(messageSender, messageReceiver);
        List<Message> alMessages = messageRepository.findAll();
        List<String> messagesList = new ArrayList<>();
        for (Message message : alMessages) {
            if ((message.getMessageSender().equals(messageSender) && (message.getMessageReceiver().equals(messageReceiver)) ||
                    message.getMessageSender().equals(messageReceiver) && (message.getMessageReceiver().equals(messageSender)))) {
                messagesList.add(message.getTextMessage());
            }
        }
        return messagesList;
    }

    public UUID insert(MessageDTO messageDTO) {
        Message message = MessageBuilder.toEntity(messageDTO);
        message = messageRepository.save(message);
        message.setRead(false);
        LOGGER.debug("Message with id {} was inserted in db", message.getId());
        return message.getId();
    }

    public void delete(String messageSender, String messageReceiver) {
        messageRepository.deleteMessageByMessageSenderAndMessageReceiver(messageSender, messageReceiver);
        LOGGER.debug("Messages sent by {} was deleted from db", messageSender);
    }

    public void markAsRead(String messageSender, String messageReceiver) {
        List<Message> allMessages = messageRepository.findAll();
        List<Message> messagesList = new ArrayList<>();
        for (Message message : allMessages) {
            if ((message.getMessageSender().equals(messageSender) && (message.getMessageReceiver().equals(messageReceiver)) ||
                    message.getMessageSender().equals(messageReceiver) && (message.getMessageReceiver().equals(messageSender)))) {
                messagesList.add(message);
            }
        }

        List<Message> unreadMessages = new ArrayList<>();
        int unreadMess = 0;
        for (Message message : messagesList) {
            if (!message.isRead()) {
                unreadMess++;
                unreadMessages.add(message);
                message.setRead(true);
                Message save = messageRepository.save(message);
            }
        }

        boolean client = false;
        boolean admin = false;
        List<Client> clientList = clientRepository.findAll();
        for (Client c : clientList) {
            if (c.getUsername().equals(messageSender)) {
                client = true;
            }
        }
        List<Admin> adminList = adminRepository.findAll();
        for (Admin a : adminList) {
            if (a.getUsername().equals(messageSender)) {
                admin = true;
            }
        }
        boolean sem = true;
        String allMessagesSender = unreadMessages.get(0).getMessageSender();
        for (Message message : unreadMessages) {
            if (!message.getMessageSender().equals(allMessagesSender)) {
                sem = false;
            }
        }
        if (unreadMess > 0 && sem == true) {
            if (admin == true) {
                System.out.println("aici admin");
                this.template.convertAndSend(NotificationEndpoints.ADMIN_HAS_READ, "Admin has read your messages");

            } else if (client == true) {
                System.out.println("aici client");
                this.template.convertAndSend(NotificationEndpoints.CLIENT_HAS_READ, messageSender);

            }
        }
    }
}
