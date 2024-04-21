package com.JavaProject.BankTransactionService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "limits")
public class Limit extends BaseModel{
    private BigDecimal limitSum;
    private LocalDateTime limitDateTime;
//    private String limitCurrencyShortName;
    private String expenseCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
