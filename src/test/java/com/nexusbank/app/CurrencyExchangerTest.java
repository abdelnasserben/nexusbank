package com.nexusbank.app;

import com.nexusbank.constant.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangerTest {

    @Test
    void shouldExchangeTwentyEurToKmf() {
        double expectedAmount = CurrencyExchanger.exchange(Currency.EUR.name(), Currency.KMF.name(), 20);
        assertEquals(9806.2, expectedAmount);
    }

    @Test
    void shouldExchangeKmfToEur() {
        double expectedAmount = CurrencyExchanger.exchange(Currency.KMF.name(), Currency.EUR.name(), 4951);
        assertEquals(10, expectedAmount);
    }

    @Test
    void shouldExchangeUsdToKmf() {
        double expectedAmount = CurrencyExchanger.exchange(Currency.USD.name(), Currency.KMF.name(), 50);
        assertEquals(22825.5, expectedAmount);
    }

    @Test
    void shouldExchangeKmfToUsd() {
        double expectedAmount = CurrencyExchanger.exchange(Currency.KMF.name(), Currency.USD.name(), 9242.4);
        assertEquals(20, expectedAmount);
    }
}