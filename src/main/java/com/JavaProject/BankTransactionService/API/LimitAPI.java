package com.JavaProject.BankTransactionService.API;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.UserRepository;
import com.JavaProject.BankTransactionService.service.serviceImpl.LimitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> setLimit(@RequestBody Limit limit) {
        User user = limit.getUser();
        String expenseCategory = limit.getExpenseCategory();
        BigDecimal newLimit = limit.getLimitSum();

        boolean isLimitSet = limitService.setLimit(user, expenseCategory, newLimit);
        if (isLimitSet) {
            return ResponseEntity.ok("Limit successfully set");
        } else {
            return ResponseEntity.badRequest().body("Limit already set for this category");
        }
    }

    @GetMapping("/{userId}/{expenseCategory}")
    public BigDecimal getLimit(@PathVariable(value = "userId") Long userId, @PathVariable(value = "expenseCategory") String expenseCategory) {
        User user = userRepository.findById(userId).orElseThrow();
        return limitService.getMonthlyLimit(user, expenseCategory);
    }
}
