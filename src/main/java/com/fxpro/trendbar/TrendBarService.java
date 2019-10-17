package com.fxpro.trendbar;

import java.util.Set;

public interface TrendBarService {
    void addQuote(Quote quote);
    Set<TrendBar> buildTrendBarsHistory(Symbol symbol);
}
