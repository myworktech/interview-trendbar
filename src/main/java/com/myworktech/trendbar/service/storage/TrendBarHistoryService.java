package com.myworktech.trendbar.service.storage;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;

import java.time.LocalDateTime;
import java.util.Set;

public interface TrendBarHistoryService {
    Set<CompletedTrendBar> buildTrendBarsHistory(Symbol symbol, TrendBarType trendBarType, LocalDateTime from) throws NoSuchTrendBarException;

    Set<CompletedTrendBar> buildTrendBarsHistory(Symbol symbol, TrendBarType trendBarType, LocalDateTime from, LocalDateTime to) throws NoSuchTrendBarException;
}
