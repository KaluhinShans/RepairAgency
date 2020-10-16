package com.shans.kaluhin.entity.enums;

public enum OrderStatus {
    VERIFICATION, // awaiting manager check
    PAYMENT, // awaiting user payment
    PENDING, // awaiting master check
    PROCESS, // master working...
    DONE,
    REJECT,
}
