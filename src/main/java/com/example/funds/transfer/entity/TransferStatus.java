package com.example.funds.transfer.entity;

public enum TransferStatus {
    TRANSFER_OK("Ok"),
    TRANSFER_SUCCESS("SUCCESS"),
    TRANSFER_ERROR("ERROR"),
    LIMIT_EXCEEDED("limit_exceeded"),
    SAME_ACCOUNT("same_account"),
    INSUFFICIENT_FUNDS("insufficient_funds"),
    ACCOUNT_NOT_FOUND("account_not_found");

    public final String label;
    TransferStatus(String label) {
        this.label = label;
    }
}
