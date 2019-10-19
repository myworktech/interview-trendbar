package com.myworktech.trendbar.model;


import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
    public String toString() {
        return symbol + "-" + trendBarType;
    }
}
