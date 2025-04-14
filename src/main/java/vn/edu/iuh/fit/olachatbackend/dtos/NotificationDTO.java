/*
 * @ (#) NotificationDTO.java       1.0     14/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.dtos;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/04/2025
 * @version:    1.0
 */

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.edu.iuh.fit.olachatbackend.enums.NotificationType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationDTO {
    private String id;
    private String title;
    private String body;
    private String senderId;
    private String receiverId;
    private NotificationType type;
    private boolean read;
    private LocalDateTime createdAt;
}
