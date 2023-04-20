package ro.tuc.ds2020.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "MESSAGE")
public class Message {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "messageSender", nullable = false)
    private String messageSender;

    @Column(name = "textMessage", nullable = false)
    private String textMessage;

    @Column(name = "messageReceiver", nullable = false)
    private String messageReceiver;

    @Column(name = "isRead")
    private boolean isRead;

    public Message(String messageSender, String textMessage, String messageReceiver, boolean isRead) {
        this.messageSender = messageSender;
        this.textMessage = textMessage;
        this.messageReceiver = messageReceiver;
        this.isRead = this.isRead();
    }

    public Message() {

    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageSender='" + messageSender + '\'' +
                ", textMessage='" + textMessage + '\'' +
                ", messageReceiver='" + messageReceiver + '\'' +
                '}';
    }
}
