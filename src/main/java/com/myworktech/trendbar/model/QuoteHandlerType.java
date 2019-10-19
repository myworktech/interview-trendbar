package com.myworktech.trendbar.model;


import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ToString
public class QuoteHandlerType {

    private static final ConcurrentMap<String, QuoteHandlerType> instances = new ConcurrentHashMap<>();

    @Getter
    private final Symbol symbol;
    @Getter
    private final TrendBarType trendBarType;

    private QuoteHandlerType(Symbol symbol, TrendBarType trendBarType) {
        this.symbol = symbol;
        this.trendBarType = trendBarType;
    }

    public static QuoteHandlerType getInstance(Symbol symbol, TrendBarType trendBarType) {
        String key = symbol.toString() + trendBarType.name();
        QuoteHandlerType quoteHandlerType = instances.get(key);
        if (quoteHandlerType != null)
            return quoteHandlerType;

        QuoteHandlerType newValue = new QuoteHandlerType(symbol, trendBarType);

        QuoteHandlerType instance = instances.putIfAbsent(key, newValue);

        return instance == null ? newValue : instance;
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
