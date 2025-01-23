/*
 * @ (#) Message.java       1.0     24/01/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.entity;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 24/01/2025
 * @version:    1.0
 */

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.iuh.fit.olachatbackend.enumvalue.MessageStatus;

import java.io.Serializable;

@Document
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    @Id
    private String id;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private MessageStatus status;

}
