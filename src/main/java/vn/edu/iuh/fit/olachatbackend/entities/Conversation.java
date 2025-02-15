package vn.edu.iuh.fit.olachatbackend.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.olachatbackend.enums.ConversationType;

import java.util.Date;

@Document(collection = "conversations")
public class Conversation {
    @Id
    private String id;

    private String avatar;
    private ConversationType type;

    private LastMessage lastMessage;
    private Date createdAt;
    private Date updatedAt;

}

@Data
class LastMessage {
    private String messageId;
    private String content;
    private Date createdAt;
}
