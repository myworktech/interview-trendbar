package com.myworktech.trendbar.service;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.Symbol;

import java.util.Set;

public interface TrendBarService {
    void addQuote(Quote quote, Callback callback);
    Set<CompletedTrendBar> buildTrendBarsHistory(Symbol symbol);
}
