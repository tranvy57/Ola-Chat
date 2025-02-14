/*
 * @ (#) Message.java       1.0     24/01/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.entities;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 24/01/2025
 * @version:    1.0
 */

import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.olachatbackend.enums.MessageStatus;
import vn.edu.iuh.fit.olachatbackend.enums.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    @Id
    private ObjectId id;
    private Long senderId;
    private ObjectId conversationId;
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private String mediaUrl;
    private MessageStatus status;
    private List<DeliveryStatus> deliveryStatus;
    private List<ReadStatus> readStatus;
    private LocalDateTime createdAt;

}
