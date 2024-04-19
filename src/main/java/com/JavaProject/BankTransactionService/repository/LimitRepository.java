package com.JavaProject.BankTransactionService.repository;

import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LimitRepository extends JpaRepository<Limit,Long> {
    Limit findTopByUserAndExpenseCategoryOrderByLimitDateTimeDesc(User user, String expenseCategory);
}
