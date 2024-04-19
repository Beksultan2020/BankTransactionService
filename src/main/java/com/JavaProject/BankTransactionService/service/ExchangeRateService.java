package com.JavaProject.BankTransactionService.service;

import java.math.BigDecimal;

public interface ExchangeRateService {
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);

    BigDecimal getRateFromExternalAPI(String fromCurrency, String toCurrency);
}
