package com.example.funds.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AccountRequest {
    @JsonProperty("account")
    String account;

    public AccountRequest() {
        super();
    }

    public AccountRequest(String account) {
        this.account = account;
    }
}
