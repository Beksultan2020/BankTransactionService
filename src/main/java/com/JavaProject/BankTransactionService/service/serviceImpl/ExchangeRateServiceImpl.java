package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.model.ExchangeRate;
import com.JavaProject.BankTransactionService.repository.ExchangeRepository;
import com.JavaProject.BankTransactionService.service.ExchangeRateService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private WebClient webClient;

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> optionalExchangeRate = exchangeRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
        if (optionalExchangeRate.isPresent()){
            return optionalExchangeRate.get().getRate();
        }else {
            BigDecimal rate=getRateFromExternalAPI(fromCurrency,toCurrency);
            ExchangeRate exchangeRate=new ExchangeRate(fromCurrency,toCurrency,rate);
            exchangeRepository.save(exchangeRate);
            return rate;
        }
    }

    @Override
    public BigDecimal getRateFromExternalAPI(String fromCurrency, String toCurrency) {
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("api.exchangeratesapi.io")
                        .path("latest")
                        .queryParam("base",fromCurrency)
                        .queryParam("symbols",toCurrency)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonObject jsonObject=new JsonParser().parse(response).getAsJsonObject();
        BigDecimal rate=jsonObject.get("rates").getAsJsonObject().get(toCurrency).getAsBigDecimal();

        return rate;
    }
}
