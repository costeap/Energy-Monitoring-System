package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.MessageRequest;
import ro.tuc.ds2020.dtos.MessageDTO;
import ro.tuc.ds2020.entities.Message;

import java.util.LinkedList;
import java.util.List;

public class MessageBuilder {

    private MessageBuilder() {
    }

    public static MessageDTO toMessageDTO(MessageRequest messageRequest) {
        return new MessageDTO(messageRequest.getMessageSender(), messageRequest.getTextMessage(), messageRequest.getMessageReceiver(), false);
    }

    public static MessageRequest toMessageRequest(MessageDTO messageDTO) {
        MessageRequest messageRequest = MessageRequest.newBuilder().setMessageSender(messageDTO.getMessageSender()).setTextMessage(messageDTO.getTextMessage()).setMessageReceiver(messageDTO.getMessageReceiver()).build();
        return messageRequest;
    }

    public static Message toEntity(MessageDTO messageDTO) {
        return new Message(messageDTO.getMessageSender(), messageDTO.getTextMessage(), messageDTO.getMessageReceiver(), messageDTO.isRead());
    }

    public static List<MessageDTO> mapMessagesToDetails(List<Message> messages) {
        List<MessageDTO> dtos = new LinkedList<>();
        for (Message message : messages) {
            dtos.add(MessageDTO.builder().messageSender(message.getMessageSender()).textMessage(message.getTextMessage()).messageReceiver(message.getMessageReceiver()).build());
        }
        return dtos;
    }

}
