package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.LimitRepository;
import com.JavaProject.BankTransactionService.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LimitServiceImpl implements LimitService {

    @Autowired
    private LimitRepository limitRepository;
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
