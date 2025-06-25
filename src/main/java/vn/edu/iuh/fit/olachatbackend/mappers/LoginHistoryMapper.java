/*
 * @ (#) LoginHistoryMapper.java       1.0     30/03/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.mappers;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 30/03/2025
 * @version:    1.0
 */

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.olachatbackend.dtos.LoginHistoryDTO;
import vn.edu.iuh.fit.olachatbackend.entities.LoginHistory;

@Mapper(componentModel = "spring")
public interface LoginHistoryMapper {

    LoginHistoryMapper INSTANCE = Mappers.getMapper(LoginHistoryMapper.class);

    @Mapping(source = "user.id", target = "userId")
    LoginHistoryDTO toDTO(LoginHistory loginHistory);

    @Mapping(source = "userId", target = "user.id")
    LoginHistory toEntity(LoginHistoryDTO loginHistoryDTO);
}
