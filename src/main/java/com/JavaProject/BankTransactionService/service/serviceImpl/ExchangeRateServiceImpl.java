package com.JavaProject.BankTransactionService.service.serviceImpl;

import com.JavaProject.BankTransactionService.model.ExchangeRate;
import com.JavaProject.BankTransactionService.repository.ExchangeRepository;
import com.JavaProject.BankTransactionService.service.ExchangeRateService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRepository exchangeRepository;
    private final WebClient webClient;


    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        Optional<ExchangeRate> optionalExchangeRate = exchangeRepository.findByFromCurrencyAndToCurrencyOrderByDateDesc(fromCurrency, toCurrency);
        if (optionalExchangeRate.isPresent()) {
            return optionalExchangeRate.get().getRate();
        } else {
            return fetchAndSaveExchangeRate(fromCurrency, toCurrency);
        }
    }

    @Override
    public BigDecimal fetchAndSaveExchangeRate(String fromCurrency, String toCurrency) {
        String apiKey = "bfb6655d2d354c82a8b72468e56ce94c";
        String apiUrl = String.format("/time_series?symbol=%s/%s&interval=1day&outputsize=1&apikey=%s",
                fromCurrency, toCurrency, apiKey);

        Mono<String> responseMono = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class);

        return responseMono.flatMap(response -> {
            BigDecimal rate = parseExchangeRateFromResponse(response);

            if (rate == null) {
                rate = BigDecimal.ZERO;
            }

            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setFromCurrency(fromCurrency);
            exchangeRate.setToCurrency(toCurrency);
            exchangeRate.setRate(rate);
            exchangeRate.setDate(LocalDate.now());
            exchangeRepository.save(exchangeRate);

            return Mono.just(rate);
        }).block();
    }

    @Override
    public BigDecimal parseExchangeRateFromResponse(String response) {
        try {
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            if (jsonObject.has("values") && !jsonObject.get("values").isJsonNull()) {
                JsonObject values = jsonObject.getAsJsonObject("values");

                if (values.has("last") && !values.get("last").isJsonNull()) {
                    JsonObject lastValue = values.getAsJsonObject("last");

                    if (lastValue.has("close") && !lastValue.get("close").isJsonNull()) {
                        BigDecimal rate = lastValue.getAsJsonPrimitive("close").getAsBigDecimal();
                        return rate;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
