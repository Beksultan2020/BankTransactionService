package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.dto.TransactionDto;
import com.JavaProject.BankTransactionService.mapper.TransactionMapper;
import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.Transaction;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.TransactionRepository;
import com.JavaProject.BankTransactionService.repository.UserRepository;
import com.JavaProject.BankTransactionService.service.ExchangeRateService;
import com.JavaProject.BankTransactionService.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;
    private final LimitServiceImpl limitService;
    private final ExchangeRateService exchangeRateService;

    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactionMapper.toDtoList(transactions);
    }


    @Override
    public List<TransactionDto> getTransactionsExceedingLimit(Long userId, String expenseCategory) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Transaction> transactions = transactionRepository.findByUserAndExpenseCategory(user, expenseCategory);
        return transactionMapper.toDtoList(transactions);
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        return transactionMapper.toDto(transaction);
    }

    @Override
    public void createTransaction(Transaction transaction) {
        BigDecimal sumInUsd = exchangeRateService.getExchangeRate(transaction.getCurrencyShortname(), "USD").multiply(transaction.getSum());

        Limit lastLimit = limitService.getLastLimit(transaction.getUser(), transaction.getExpenseCategory());
        if (lastLimit != null) {
            if (sumInUsd.compareTo(lastLimit.getLimitSum()) > 0) {
                transaction.setLimitExceeded(true);
            }
        }

        User user = userRepository.findById(transaction.getUser().getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        transaction.setUser(user);
        transactionRepository.save(transaction);
    }


        @Override
    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }
}
