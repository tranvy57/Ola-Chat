/*
 * @ (#) LoginHistoryService.java       1.0     30/03/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.services;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 30/03/2025
 * @version:    1.0
 */

import vn.edu.iuh.fit.olachatbackend.dtos.LoginHistoryDTO;

import java.util.List;

public interface LoginHistoryService {
    void saveLogin(String userId, String userAgent);
    void saveLogout(String userId);
    LoginHistoryDTO getRecentLogin(String userId);
    boolean isUserOnline(String userId);
    List<LoginHistoryDTO> getLoginHistory(String userId);

    void pingOnline(String userId);
    void checkTimeoutUsers();

}
