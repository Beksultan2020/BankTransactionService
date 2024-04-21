package com.JavaProject.BankTransactionService.API;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.UserRepository;
import com.JavaProject.BankTransactionService.service.serviceImpl.LimitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/limits")
public class LimitAPI {
    @Autowired
    private LimitServiceImpl limitService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public void setLimit(@RequestBody Limit limit) {
        limitService.setLimit(limit.getUser(), limit.getExpenseCategory(), limit.getLimitSum());
    }

    @GetMapping("/{userId}/{expenseCategory}")
    public BigDecimal getLimit(@PathVariable Long userId, @PathVariable String expenseCategory) {
        User user = userRepository.findById(userId).orElseThrow();
        return limitService.getLimit(user, expenseCategory);
    }
}
