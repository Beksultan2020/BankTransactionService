package com.JavaProject.BankTransactionService.service;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface LimitService {
    void setLimit(User user, String expenseCategory, BigDecimal newLimit);

    Limit getLastLimit(User user, String expenseCategory);

    BigDecimal getMonthlyLimit(User user, String expenseCategory);
}
