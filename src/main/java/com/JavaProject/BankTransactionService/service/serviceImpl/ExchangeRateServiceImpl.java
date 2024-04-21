package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.model.ExchangeRate;
import com.JavaProject.BankTransactionService.repository.ExchangeRepository;
import com.JavaProject.BankTransactionService.service.ExchangeRateService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private WebClient webClient;

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> optionalExchangeRate = exchangeRepository.findByFromCurrencyAndToCurrencyOrderByDateDesc(fromCurrency, toCurrency);
        if (optionalExchangeRate.isPresent()) {
            return optionalExchangeRate.get().getRate();
        } else {
            BigDecimal rate = getRateFromExternalAPI(fromCurrency, toCurrency);
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setFromCurrency(fromCurrency);
            exchangeRate.setToCurrency(toCurrency);
            exchangeRate.setRate(rate);
            exchangeRate.setDate(LocalDate.now());
            exchangeRepository.save(exchangeRate);
            return rate;
        }
    }

    @Override
    public BigDecimal getRateFromExternalAPI(String fromCurrency, String toCurrency) {
        String apiKey = "bfb6655d2d354c82a8b72468e56ce94c";
        String url = String.format("/time_series?symbol=%s/%s&interval=1day&outputsize=1&apikey=%s",
                fromCurrency, toCurrency, apiKey);

        Mono<String> responseMono = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);

        return responseMono.flatMap(response -> {
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            if (jsonObject.has("values")) {
                JsonArray values = jsonObject.get("values").getAsJsonArray();
                if (!values.isEmpty()) {
                    JsonObject firstValue = values.get(0).getAsJsonObject();
                    if (firstValue.has("close")) {
                        JsonElement rateElement = firstValue.get("close");
                        if (rateElement.isJsonPrimitive() && rateElement.getAsJsonPrimitive().isNumber()) {
                            BigDecimal rate = rateElement.getAsBigDecimal();
                            return Mono.just(rate);
                        } else {
                            return Mono.error(new RuntimeException("The 'close' field is not a number"));
                        }
                    } else {
                        return Mono.error(new RuntimeException("Response from API does not contain 'close' field"));
                    }
                } else {
                    return Mono.error(new RuntimeException("Response from API does not contain any values"));
                }
            } else {
                return Mono.error(new RuntimeException("Response from API does not contain 'values' field. Please check the API response structure or the request parameters."));
            }
        }).block();
    }
}
