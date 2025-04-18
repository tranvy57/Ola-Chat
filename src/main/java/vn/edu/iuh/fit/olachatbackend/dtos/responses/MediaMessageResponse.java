/*
 * @ (#) MediaMessageResponse.java       1.0     17/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.dtos.responses;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 17/04/2025
 * @version:    1.0
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaMessageResponse {
    private String id;
    private List<String> mediaUrls;
    private String type;
    private String senderId;
    private LocalDateTime createdAt;
}
