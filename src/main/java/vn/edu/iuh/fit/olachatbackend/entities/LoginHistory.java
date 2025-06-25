/*
 * @ (#) LoginHistory.java       1.0     30/03/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.entities;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 30/03/2025
 * @version:    1.0
 */

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.edu.iuh.fit.olachatbackend.enums.LoginHistoryStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;

    @Enumerated(EnumType.STRING)
    private LoginHistoryStatus status;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "last_ping_time")
    private LocalDateTime lastPingTime;

}
