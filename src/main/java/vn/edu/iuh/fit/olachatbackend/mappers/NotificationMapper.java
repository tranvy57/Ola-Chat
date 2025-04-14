/*
 * @ (#) NotificationMapper.java       1.0     14/04/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.mappers;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 14/04/2025
 * @version:    1.0
 */

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.olachatbackend.dtos.NotificationDTO;
import vn.edu.iuh.fit.olachatbackend.entities.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "read", target = "read")
    NotificationDTO toDTO(Notification notification);

    Notification toEntity(NotificationDTO notificationDTO);
}



