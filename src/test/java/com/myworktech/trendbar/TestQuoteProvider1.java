package com.myworktech.trendbar;

import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.service.QuoteProvider;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class TestQuoteProvider1 implements QuoteProvider {

    @Override
    public Quote getQuote() {
        return new Quote(nextSymbol(), nextPrice(), LocalDateTime.now());
//        return new Quote(Symbol.getInstance("USDRUB"), nextPrice(), LocalDateTime.now());
    }

    private AtomicReference<Long> priceCounter = new AtomicReference<>(null);
    private AtomicReference<Integer> symbolCounter = new AtomicReference<>(null);

    public Long nextPrice() {
        Long oldValue;
        Long newValue;
        do {
            oldValue = priceCounter.get();
            newValue = oldValue == null ? 1L : oldValue + 1;
        } while (!priceCounter.compareAndSet(oldValue, newValue));
        return newValue;
    }

    public Symbol nextSymbol() {
        Integer oldValue;
        Integer newValue;
        do {
            oldValue = symbolCounter.get();
            newValue = oldValue == null ? 1 : oldValue * (-1);
        } while (!symbolCounter.compareAndSet(oldValue, newValue));
        return newValue > 0 ? Symbol.getInstance("USDEUR") : Symbol.getInstance("USDRUB");
    }
}
