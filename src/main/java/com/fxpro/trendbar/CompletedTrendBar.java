package com.fxpro.trendbar;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"openPrice", "closePrice", "quotesCount"})
public class CompletedTrendBar implements TrendBar {

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


    @Override
    public TrendBarType getType() {
        return trendBarType;
    }

    @Override
    public Symbol getTrendBarSymbol() {
        return symbol;
    }
}
