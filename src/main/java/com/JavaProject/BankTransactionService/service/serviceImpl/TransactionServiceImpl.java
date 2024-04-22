package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.dto.TransactionDto;
import com.JavaProject.BankTransactionService.mapper.TransactionMapper;
import com.JavaProject.BankTransactionService.model.Limit;
import com.JavaProject.BankTransactionService.model.Transaction;
import com.JavaProject.BankTransactionService.model.User;
import com.JavaProject.BankTransactionService.repository.LimitRepository;
import com.JavaProject.BankTransactionService.repository.TransactionRepository;
import com.JavaProject.BankTransactionService.repository.UserRepository;
import com.JavaProject.BankTransactionService.service.ExchangeRateService;
import com.JavaProject.BankTransactionService.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;
    private final LimitServiceImpl limitService;
    private final ExchangeRateService exchangeRateService;
    private final LimitRepository limitRepository;

    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactionMapper.toDtoList(transactions);
    }

    @Override
    public List<TransactionDto> getTransactionsExceedingLimit(Long userId, String expenseCategory) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Transaction> transactions = transactionRepository.findByUserAndExpenseCategory(user, expenseCategory);

        List<Transaction> exceededTransactions = transactions.stream()
                .filter(transaction -> transaction.isLimitExceeded())
                .collect(Collectors.toList());

        return exceededTransactions.stream()
                .map(transaction -> {
                    TransactionDto dto = transactionMapper.toDto(transaction);
                    Limit limit = limitRepository.findTopByUserAndExpenseCategoryOrderByLimitDateTimeDesc(user, expenseCategory);
                    if (limit != null) {
                        dto.setLimitSum(limit.getLimitSum());
                        dto.setLimitDateTime(limit.getLimitDateTime());
                        dto.setLimitCurrencyShortname("USD");
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        return transactionMapper.toDto(transaction);
    }

    @Override
    public void createTransaction(Transaction transaction) {
        BigDecimal sumInUsd = exchangeRateService.getExchangeRate(transaction.getCurrencyShortname(), "USD")
                .multiply(transaction.getSum());

        List<Limit> limits = limitRepository.findByUserAndExpenseCategoryOrderByLimitDateTimeDesc(transaction.getUser(), transaction.getExpenseCategory());

        boolean limitExceeded = false;
        boolean monthlyLimitExceeded = false;
        for (Limit limit : limits) {
            if (sumInUsd.compareTo(limit.getLimitSum()) > 0) {
                limitExceeded = true;
                BigDecimal monthlyLimit = limitService.getMonthlyLimit(transaction.getUser(), transaction.getExpenseCategory());
                if (monthlyLimit != null && sumInUsd.compareTo(monthlyLimit) > 0) {
                    monthlyLimitExceeded = true;
                }
                break;
            }
        }

        transaction.setLimitExceeded(limitExceeded);
        transaction.setLimitExceededMonthly(monthlyLimitExceeded);
        transactionRepository.save(transaction);
    }


    @Override
    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }
}
