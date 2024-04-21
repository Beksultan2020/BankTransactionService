package com.JavaProject.BankTransactionService.service;

import com.JavaProject.BankTransactionService.dto.TransactionDto;
import com.JavaProject.BankTransactionService.model.Transaction;
import com.JavaProject.BankTransactionService.model.User;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getTransactionsExceedingLimit(Long userId, String expenseCategory);

    TransactionDto getTransactionById(Long id);

    void createTransaction(Transaction transaction);

    void deleteTransactionById(Long id);
}
