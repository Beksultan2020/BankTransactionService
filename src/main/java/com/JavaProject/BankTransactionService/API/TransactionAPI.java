package com.JavaProject.BankTransactionService.API;


import com.JavaProject.BankTransactionService.dto.TransactionDto;
import com.JavaProject.BankTransactionService.dto.UserDto;
import com.JavaProject.BankTransactionService.model.Transaction;
import com.JavaProject.BankTransactionService.service.serviceImpl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionAPI {
    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionServiceImpl.getAllTransactions();
    }

    @GetMapping("{id}")
    public TransactionDto getTransactionById(@PathVariable(value = "id") Long id) {
        return transactionServiceImpl.getTransactionById(id);
    }
    @GetMapping("/exceededLimit")
    public List<TransactionDto> getTransactionsExceedingLimit(@RequestBody UserDto userDto){
        return transactionServiceImpl.getTransactionsExceedingLimit(userDto);
    }

    @PostMapping
    public TransactionDto createTransaction(@RequestBody Transaction transaction) {
        return transactionServiceImpl.createTransaction(transaction);
    }

    @DeleteMapping("{id}")
    public void deleteTransaction(@PathVariable(value = "id") Long id) {
        transactionServiceImpl.deleteTransactionById(id);
    }

}
