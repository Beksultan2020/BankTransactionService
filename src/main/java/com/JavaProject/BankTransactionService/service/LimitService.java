package com.JavaProject.BankTransactionService.service;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;

import java.math.BigDecimal;

public interface LimitService {
    boolean setLimit(User user, String expenseCategory, BigDecimal newLimit);

    Limit getLastLimit(User user, String expenseCategory);

    BigDecimal getMonthlyLimit(User user, String expenseCategory);
}
