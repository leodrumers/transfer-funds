package com.example.funds.transfer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferResponse {
    String status;
    List<String> errors;

    @JsonProperty(value = "tax_collected")
    BigDecimal taxCollected;
    @JsonProperty("CAD")
    BigDecimal cad;
    String description;

}
