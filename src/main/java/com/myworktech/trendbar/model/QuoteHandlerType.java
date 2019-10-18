package com.myworktech.trendbar.model;


import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ToString
public class QuoteHandlerType {

    private static final ConcurrentMap<QuoteHandlerType, Object> instances = new ConcurrentHashMap<>();
    private static final Object PRESENT = new Object();

    static {
//        instances.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDEUR"), TrendBarType.S1));
    }

    @Getter
    private final Symbol symbol;
    @Getter
    private final TrendBarType trendBarType;

    private QuoteHandlerType(Symbol symbol, TrendBarType trendBarType) {
        this.symbol = symbol;
        this.trendBarType = trendBarType;
    }

    public static QuoteHandlerType getInstance(Symbol symbol, TrendBarType trendBarType) {
        Optional<QuoteHandlerType> quoteHandlerType = instances.keySet().stream().filter(q -> q.getSymbol() == symbol && q.getTrendBarType().equals(trendBarType)).findFirst();
        if (quoteHandlerType.isPresent())
            return quoteHandlerType.get();


        QuoteHandlerType newValue = new QuoteHandlerType(symbol, trendBarType);

        Object instance = instances.putIfAbsent(newValue, PRESENT);

        return instance == null ? newValue : quoteHandlerType.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuoteHandlerType that = (QuoteHandlerType) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(trendBarType, that.trendBarType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, trendBarType);
    }
}
