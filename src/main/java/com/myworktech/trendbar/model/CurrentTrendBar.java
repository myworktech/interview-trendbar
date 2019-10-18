package com.myworktech.trendbar.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CurrentTrendBar {

    @Getter
    private final Deque<Quote> quoteSet = new ConcurrentLinkedDeque<>();

    @Getter
    private final LocalDateTime timeStamp;
    private final TrendBarType trendBarType;
    private final Symbol symbol;

    public CurrentTrendBar(TrendBarType trendBarType, Quote quote) {
        this.timeStamp = LocalDateTime.now();
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
        Optional<Quote> quoteOptional = quoteSet.stream().min(Comparator.comparing(Quote::getTimeStamp));
        return quoteOptional.map(Quote::getPrice).orElse(0L);
    }

    public long getClosePrice() {
        return quoteSet.stream().max(Comparator.comparing(Quote::getTimeStamp)).get().getPrice();
    }

    public long getLowPrice() {
        return quoteSet.stream().min(Comparator.comparing(Quote::getPrice)).get().getPrice();
    }

    public long getHighPrice() {
        return quoteSet.stream().max(Comparator.comparing(Quote::getPrice)).get().getPrice();
    }

    public TrendBarType getType() {
        return trendBarType;
    }

    public Symbol getTrendBarSymbol() {
        return symbol;
    }


}
