package com.JavaProject.BankTransactionService.repository;

import com.JavaProject.BankTransactionService.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findFirstByFromCurrencyAndToCurrencyOrderByDateDesc(String fromCurrency, String toCurrency);
}
