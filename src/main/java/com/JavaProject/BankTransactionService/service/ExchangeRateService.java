package com.JavaProject.BankTransactionService.service;

import java.math.BigDecimal;

public interface ExchangeRateService {
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);

    BigDecimal fetchAndSaveExchangeRate(String fromCurrency, String toCurrency);

    BigDecimal parseExchangeRateFromResponse(String response);
}
