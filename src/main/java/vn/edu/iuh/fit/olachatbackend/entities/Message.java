package vn.edu.iuh.fit.olachatbackend.entities;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.olachatbackend.enums.MessageStatus;
import vn.edu.iuh.fit.olachatbackend.enums.MessageType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String senderId;
    private String conversationId;
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private List<DeliveryStatus> deliveryStatus;
    private List<ReadStatus> readStatus;
    private List<ReplyStatus> replyStatus;
    private List<DeletedStatus> deletedStatus;

    private Date createdAt;


}
@Data
class DeliveryStatus {
    private String userId;
    private Date deliveredAt;
}

@Data
class ReadStatus {
    private String userId;
    private Date readAt;
}

@Data
class ReplyStatus {
    private String userId;
    private Date repliedAt;
    private String replyMessageId;
}

@Data
class DeletedStatus {
    private String userId;
    private Date deletedAt;
}

