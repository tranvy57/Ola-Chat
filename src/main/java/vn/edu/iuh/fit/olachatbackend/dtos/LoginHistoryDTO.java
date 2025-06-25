/*
 * @ (#) LoginHistoryDTO.java       1.0     30/03/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.dtos;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 30/03/2025
 * @version:    1.0
 */

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistoryDTO {
    private String userId;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String status;
    private String userAgent;
}
