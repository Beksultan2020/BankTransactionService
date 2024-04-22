package com.JavaProject.BankTransactionService.API;

import com.JavaProject.BankTransactionService.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRateAPI {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/{fromCurrency}/{toCurrency}")
    public BigDecimal getExchangeRate(@PathVariable(value = "fromCurrency") String fromCurrency, @PathVariable(value = "toCurrency") String toCurrency) {
        return exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
    }
}
