package com.JavaProject.BankTransactionService.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ExchangeRate extends BaseModel{
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private LocalDate date;
}
