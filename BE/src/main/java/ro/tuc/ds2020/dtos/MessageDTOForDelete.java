package ro.tuc.ds2020.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@Getter
@Setter
public class MessageDTOForDelete {
    private String messageSender;
    private String messageReceiver;

    public MessageDTOForDelete(String messageSender, String messageReceiver) {
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
    }
}
