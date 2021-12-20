package com.example.funds.transfer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transfer_history")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    private BigDecimal amount;
    private String type;
    private String description;
    private LocalDateTime transferDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @PrePersist
    public void onCreate() {
        transferDate = LocalDateTime.now();
    }
}
