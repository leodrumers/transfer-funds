package com.example.funds.transfer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "transfer_history")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transferDate;
    private String status;
    private Long originAccount;
    private Long destinationAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransferHistory that = (TransferHistory) o;
        return transferId != null && Objects.equals(transferId, that.transferId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PrePersist
    public void onCreate() {
        transferDate = LocalDateTime.now();
    }
}
