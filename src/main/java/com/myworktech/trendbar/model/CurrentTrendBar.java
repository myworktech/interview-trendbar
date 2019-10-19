package com.myworktech.trendbar.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CurrentTrendBar {

    @Getter
    private final Deque<Quote> quoteSet = new ConcurrentLinkedDeque<>();

    @Getter
    private final LocalDateTime createdTimeStamp;
    private final TrendBarType trendBarType;
    private final Symbol symbol;

    public CurrentTrendBar(TrendBarType trendBarType, Quote quote) {
        this.createdTimeStamp = LocalDateTime.now();
        this.trendBarType = trendBarType;
        this.symbol = quote.getSymbol();
        quoteSet.add(quote);
    }

    public Quote getLastQuote() {
        return quoteSet.getLast();
    }

    public void addQuote(Quote quote) {
        quoteSet.add(quote);
    }

    public long getOpenPrice() {
        return quoteSet.stream().min(Comparator.comparing(Quote::getTimeStamp)).map(Quote::getPrice).orElse(0L);
    }

    public long getClosePrice() {
        return quoteSet.stream().max(Comparator.comparing(Quote::getTimeStamp)).map(Quote::getPrice).orElse(0L);
    }

    public long getLowPrice() {
        return quoteSet.stream().min(Comparator.comparing(Quote::getPrice)).map(Quote::getPrice).orElse(0L);
    }

    public long getHighPrice() {
        return quoteSet.stream().max(Comparator.comparing(Quote::getPrice)).map(Quote::getPrice).orElse(0L);
    }

    public TrendBarType getType() {
        return trendBarType;
    }

    public Symbol getTrendBarSymbol() {
        return symbol;
    }
}
