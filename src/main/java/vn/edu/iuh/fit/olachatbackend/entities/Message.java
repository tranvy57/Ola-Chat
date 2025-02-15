package vn.edu.iuh.fit.olachatbackend.entities;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Builder
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String senderId;
    private String conversationId;
    private String content;
    private String messageType;
    private String mediaUrl;
    private String status;

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
