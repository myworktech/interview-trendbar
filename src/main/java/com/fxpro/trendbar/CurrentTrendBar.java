package com.fxpro.trendbar;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CurrentTrendBar implements TrendBar {

    @Getter
    private final Deque<Quote> quoteSet = new ConcurrentLinkedDeque<>();

    @Getter
    private final LocalDateTime timeStamp;
    private final TrendBarType trendBarType;
    private final Symbol symbol;

    public Quote getLastQuote() {
        return quoteSet.getLast();
    }

    public CurrentTrendBar(TrendBarType m1, Quote quote) {
        this.timeStamp = LocalDateTime.now();
        this.trendBarType = m1;
        this.symbol = quote.getSymbol();
        quoteSet.add(quote);
    }

    public void addQuote(Quote quote) {
        quoteSet.add(quote);
    }


    public long getOpenPrice() {
        return quoteSet.getFirst().getPrice();
    }
    public long getClosePrice() {
        return quoteSet.getLast().getPrice();
    }


    public long getHighPrice() {
        return quoteSet.stream().max(Comparator.comparing(Quote::getTimeStamp)).get().getPrice();
    }

    public long getLowPrice() {
        return quoteSet.stream().min(Comparator.comparing(Quote::getTimeStamp)).get().getPrice();
    }


    @Override
    public TrendBarType getType() {
        return trendBarType;
    }

    @Override
    public Symbol getTrendBarSymbol() {
        return symbol;
    }


}
