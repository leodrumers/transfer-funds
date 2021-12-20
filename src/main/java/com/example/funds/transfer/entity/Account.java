package com.example.funds.transfer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class    Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private String name;

    @Column(name = "create_date")
    private LocalDateTime creationDate;

    private BigDecimal funds;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false, updatable = false)
    private Currency currency;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferHistory> transfers;

    @PrePersist
    private void onCreate() {
        creationDate = LocalDateTime.now();
    }
}
