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
        Limit limit=new Limit();
        limit.setUser(user);
        limit.setExpenseCategory(expenseCategory);
        limit.setLimitSum(newLimit);
        limit.setLimitDateTime(LocalDateTime.now());
        limitRepository.save(limit);
    }

    @Override
    public BigDecimal getLimit(User user, String expenseCategory) {
        Limit limit=limitRepository.findTopByUserAndExpenseCategoryOrderByLimitDateTimeDesc(user,expenseCategory);
        if (limit!=null){
            return limit.getLimitSum();
        }else {
           return new BigDecimal(1000.00);
        }
    }
}
