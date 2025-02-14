/*
 * @ (#) FriendRequest.java       1.0     14/02/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.olachatbackend.enums.RequestStatus;

import java.time.LocalDateTime;

/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/02/2025
 * @version:    1.0
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private Long receiverId;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime responseAt;
}
