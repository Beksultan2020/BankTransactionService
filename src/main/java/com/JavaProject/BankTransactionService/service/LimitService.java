package com.JavaProject.BankTransactionService.service;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface LimitService {
    BigDecimal getLimit(User user,String expenseCategory);

    void setLimit(User user, String expenseCategory, BigDecimal newLimit);
}
