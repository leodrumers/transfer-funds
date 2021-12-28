package com.example.funds.transfer.entity;

public enum TransferStatus {
    TRANSFER_OK("Ok"),
    TRANSFER_SUCCESS("SUCCESS"),
    TRANSFER_ERROR("ERROR"),
    NEGATIVE_AMOUNT("negative_amount"),
    LIMIT_EXCEEDED("limit_exceeded"),
    SAME_ACCOUNT("same_account"),
    INSUFFICIENT_FUNDS("insufficient_funds"),
    ACCOUNT_NOT_FOUND("account_not_found"),
    THIRD_SERVICE_ERROR("third_service_error");

    public final String label;
    TransferStatus(String label) {
        this.label = label;
    }
}
