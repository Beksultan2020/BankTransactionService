package com.JavaProject.BankTransactionService.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private String currencyShortname;
    private BigDecimal sum;
    private String expenseCategory;
    private Date dateTime;
    private boolean limitExceeded;
}
