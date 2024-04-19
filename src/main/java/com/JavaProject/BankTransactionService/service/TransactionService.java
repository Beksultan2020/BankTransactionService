package com.JavaProject.BankTransactionService.service;

import com.JavaProject.BankTransactionService.dto.TransactionDto;
import com.JavaProject.BankTransactionService.dto.UserDto;
import com.JavaProject.BankTransactionService.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getTransactionsExceedingLimit(UserDto userDto);

    TransactionDto getTransactionById(Long id);

    void addTransaction(Transaction transaction);

    void deleteTransactionById(Long id);
}
