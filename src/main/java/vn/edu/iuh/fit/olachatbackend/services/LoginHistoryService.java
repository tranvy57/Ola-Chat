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

public interface LoginHistoryService {
    void saveLogin(String userId);
    void saveLogout(String userId);
    LoginHistoryDTO getRecentLogin(String userId);
    boolean isUserOnline(String userId);
}
