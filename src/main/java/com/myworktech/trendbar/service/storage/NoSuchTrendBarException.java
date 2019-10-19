package com.myworktech.trendbar.service.storage;

import com.myworktech.trendbar.model.QuoteHandlerType;

public class NoSuchTrendBarException extends Exception {
    public NoSuchTrendBarException(QuoteHandlerType quoteHandlerType) {
        super("No trendbars found by symbol " + quoteHandlerType.getSymbol() + " and trend bar type " + quoteHandlerType.getTrendBarType());
    }
}
