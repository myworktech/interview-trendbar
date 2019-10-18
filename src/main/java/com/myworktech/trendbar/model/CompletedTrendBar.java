package com.myworktech.trendbar.model;

import lombok.Getter;

@Getter
public class CompletedTrendBar {

    private final TrendBarType trendBarType;
    private final Symbol symbol;

    private final long closePrice;
    private final long highPrice;
    private final long lowPrice;
    private final long openPrice;
    private final int quotesCount;


    public CompletedTrendBar(CurrentTrendBar currentTrendBar) {
        this.trendBarType = currentTrendBar.getType();
        this.symbol = currentTrendBar.getTrendBarSymbol();
        this.closePrice = currentTrendBar.getClosePrice();
        this.highPrice = currentTrendBar.getHighPrice();
        this.lowPrice = currentTrendBar.getLowPrice();
        this.openPrice = currentTrendBar.getOpenPrice();
        this.quotesCount = currentTrendBar.getQuoteSet().size();
    }


    public TrendBarType getType() {
        return trendBarType;
    }

    public Symbol getTrendBarSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompletedTrendBar{");
        sb.append(", lowPrice=").append(lowPrice);
        sb.append(", highPrice=").append(highPrice);
        sb.append(", quotesCount=").append(quotesCount);
        sb.append(", hashCode=").append(Integer.toHexString(hashCode()));
        sb.append('}');
        return sb.toString();
    }
}
