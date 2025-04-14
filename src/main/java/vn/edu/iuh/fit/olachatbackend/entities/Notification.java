/*
 * @ (#) Notification.java       1.0     14/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.entities;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/04/2025
 * @version:    1.0
 */

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import vn.edu.iuh.fit.olachatbackend.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String body;
    private String senderId;
    private String receiverId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "is_read")
    private boolean read;
    private LocalDateTime createdAt;

}
