package com.myworktech.trendbar.service.trendbar;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.service.Callback;

import java.util.Set;

public interface TrendBarService {
    void addQuote(Quote quote, Callback callback);
    void shutdownService() throws InterruptedException;
    Set<CompletedTrendBar> buildTrendBarsHistory(Symbol symbol);
}
