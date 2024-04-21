package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.LimitRepository;
import com.JavaProject.BankTransactionService.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class LimitServiceImpl implements LimitService {
    @Autowired
    private LimitRepository limitRepository;

    @Override
    public void setLimit(User user, String expenseCategory, BigDecimal newLimit) {
        Limit existingLimit = limitRepository.findTopByUserAndExpenseCategoryOrderByLimitDateTimeDesc(user, expenseCategory);
        if (existingLimit == null) {
            Limit limit = new Limit();
            limit.setUser(user);
            limit.setExpenseCategory(expenseCategory);
            limit.setLimitSum(newLimit);
            limit.setLimitDateTime(LocalDateTime.now());
            limitRepository.save(limit);
        } else {
            throw new IllegalArgumentException("Limit already set for this category");
        }
    }

    @Override
    public Limit getLastLimit(User user, String expenseCategory) {
        return limitRepository.findTopByUserAndExpenseCategoryOrderByLimitDateTimeDesc(user, expenseCategory);
    }

    @Override
    public BigDecimal getMonthlyLimit(User user, String expenseCategory) {
        Limit lastLimit = getLastLimit(user, expenseCategory);
        if (lastLimit != null) {
            return lastLimit.getLimitSum();
        } else {
            return new BigDecimal(1000.00);
        }
    }
}
