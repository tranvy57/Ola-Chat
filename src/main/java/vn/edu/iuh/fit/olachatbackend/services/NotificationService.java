/*
 * @ (#) NotificationService.java       1.0     06/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.services;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 06/04/2025
 * @version:    1.0
 */

import vn.edu.iuh.fit.olachatbackend.dtos.NotificationDTO;
import vn.edu.iuh.fit.olachatbackend.dtos.requests.NotificationRequest;
import vn.edu.iuh.fit.olachatbackend.entities.Notification;

import java.util.List;

public interface NotificationService {
    void registerDevice(String userId, String token);
    void sendNotification(NotificationRequest request);
    List<NotificationDTO> getNotificationsByUser(String userId);
    void markAsRead(String notificationId);
}
