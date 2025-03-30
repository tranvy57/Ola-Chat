/*
 * @ (#) LoginHistoryStatus.java       1.0     30/03/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.enums;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 30/03/2025
 * @version:    1.0
 */

import lombok.Getter;

@Getter
public enum LoginHistoryStatus {
    ONLINE("ONLINE"), OFFLINE("OFFLINE");

    private final String value;
    private LoginHistoryStatus(String value) {
        this.value = value;
    }


}
