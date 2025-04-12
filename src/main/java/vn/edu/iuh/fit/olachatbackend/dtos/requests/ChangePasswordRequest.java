/*
 * @ (#) ChangePasswordRequest.java    1.0    11/04/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package vn.edu.iuh.fit.olachatbackend.dtos.requests;/*
 * @description:
 * @author: Bao Thong
 * @date: 11/04/2025
 * @version: 1.0
 */

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
