/*
 * @ (#) MessageStatus.java       1.0     24/01/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.enums;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 24/01/2025
 * @version:    1.0
 */

import lombok.Getter;

@Getter
public enum MessageStatus {
    SENT("SENT"), RECEIVED("RECEIVED"), READ("READ");

    private final String value;

    MessageStatus(String value) {
        this.value = value;
    }
}
