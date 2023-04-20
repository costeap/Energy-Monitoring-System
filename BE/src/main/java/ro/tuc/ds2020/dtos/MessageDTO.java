package ro.tuc.ds2020.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class MessageDTO {
    private String messageSender;
    private String textMessage;
    private String messageReceiver;
    private boolean isRead;

    public MessageDTO(String messageSender, String textMessage, String messageReceiver, boolean isRead) {
        this.messageSender = messageSender;
        this.textMessage = textMessage;
        this.messageReceiver = messageReceiver;
        this.isRead = isRead;
    }
}
