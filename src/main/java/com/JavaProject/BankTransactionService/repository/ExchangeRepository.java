package com.JavaProject.BankTransactionService.repository;

import com.JavaProject.BankTransactionService.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional
public interface ExchangeRepository extends JpaRepository<ExchangeRate,Long> {
    Optional<ExchangeRate> findByFromCurrencyAndToCurrencyOrderByDateDesc(String fromCurrency, String toCurrency);
}
