package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.dto.TransactionDto;
import com.JavaProject.BankTransactionService.dto.UserDto;
import com.JavaProject.BankTransactionService.mapper.TransactionMapper;
import com.JavaProject.BankTransactionService.model.Transaction;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.TransactionRepository;
import com.JavaProject.BankTransactionService.repository.UserRepository;
import com.JavaProject.BankTransactionService.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LimitServiceImpl limitService;
    @Autowired
    private ExchangeRateServiceImpl exchangeRateService;

    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactionMapper.toDtoList(transactions);
    }

    @Override
    public List<TransactionDto> getTransactionsExceedingLimit(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        List<Transaction> transactions = transactionRepository.findByUserAndLimitExceededTrue(user);
        return transactionMapper.toDtoList(transactions);
    }


    @Override
    public TransactionDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto createTransaction(Transaction transaction) {
        BigDecimal limit = limitService.getLimit(transaction.getUser(), transaction.getExpenseCategory());
        BigDecimal sumInUsd = exchangeRateService.getExchangeRate(transaction.getCurrencyShortname(), "USD").multiply(transaction.getSum());
        if (sumInUsd.compareTo(limit) > 0) {
            transaction.setLimitExceeded(true);
        }
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }
}
