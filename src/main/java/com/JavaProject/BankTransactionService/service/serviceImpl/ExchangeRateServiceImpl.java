package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.model.ExchangeRate;
import com.JavaProject.BankTransactionService.repository.ExchangeRateRepository;
import com.JavaProject.BankTransactionService.service.ExchangeRateService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private WebClient webClient;


    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> exchangeRateOptional = exchangeRateRepository.findFirstByFromCurrencyAndToCurrencyOrderByDateDesc(fromCurrency, toCurrency);
        if (exchangeRateOptional.isPresent()) {
            return exchangeRateOptional.get().getRate();
        } else {
            return fetchAndSaveExchangeRate(fromCurrency, toCurrency);
        }
    }

    @Override
    public BigDecimal fetchAndSaveExchangeRate(String fromCurrency, String toCurrency) {

        String apiKey = "bfb6655d2d354c82a8b72468e56ce94c";
        String apiUrl = String.format("https://api.twelvedata.com/time_series?symbol=%s/%s&interval=1day&outputsize=1&apikey=%s", fromCurrency, toCurrency, apiKey);

        String response = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        BigDecimal rate = parseExchangeRateFromResponse(response);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setFromCurrency(fromCurrency);
        exchangeRate.setToCurrency(toCurrency);
        exchangeRate.setRate(rate);
        exchangeRate.setDate(LocalDate.now());
        exchangeRateRepository.save(exchangeRate);

        return rate;
    }


    private BigDecimal parseExchangeRateFromResponse(String response) {
        try {
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            if (jsonObject.has("values") && !jsonObject.get("values").isJsonNull()) {
                JsonArray values = jsonObject.getAsJsonArray("values");
                if (!values.isJsonNull() && values.size() > 0) {
                    JsonObject firstValue = values.get(0).getAsJsonObject();
                    if (firstValue.has("close") && !firstValue.get("close").isJsonNull()) {
                        return firstValue.getAsJsonPrimitive("close").getAsBigDecimal();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

}
