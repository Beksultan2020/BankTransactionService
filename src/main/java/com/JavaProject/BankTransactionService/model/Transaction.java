package com.JavaProject.BankTransactionService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction extends BaseModel {
    private String accountFrom;
    private String accountTo;
    private String currencyShortname;
    private BigDecimal sum;
    private String expenseCategory;
    private LocalDateTime dateTime;
    private boolean limitExceeded;
    private boolean limitExceededMonthly;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @PrePersist
    protected void onCreate() {
        dateTime = LocalDateTime.now();
    }
}
