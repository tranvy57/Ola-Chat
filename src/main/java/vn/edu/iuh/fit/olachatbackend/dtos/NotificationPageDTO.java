/*
 * @ (#) NotificationPageDTO.java       1.0     14/04/2025
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

import java.util.List;

@Getter
@Setter
@Builder
public class NotificationPageDTO {
    private List<NotificationDTO> content;
    private int totalPages;
    private long totalElements;
}
