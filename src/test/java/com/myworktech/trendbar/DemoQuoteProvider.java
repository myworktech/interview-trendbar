package com.myworktech.trendbar;

import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.service.QuoteProvider;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class DemoQuoteProvider implements QuoteProvider {

    private final Symbol symbol;
    private AtomicReference<Long> priceCounter = new AtomicReference<>(0L);

    public DemoQuoteProvider(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public Quote getQuote() {
        return new Quote(symbol, nextPrice(), LocalDateTime.now());
    }

    private Long nextPrice() {
        Long oldValue;
        Long newValue;
        do {
            oldValue = priceCounter.get();
            newValue = oldValue + 1;
        } while (!priceCounter.compareAndSet(oldValue, newValue));
        return newValue;
    }
}
