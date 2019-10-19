package com.myworktech.trendbar.service.trendbar;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;
import com.myworktech.trendbar.service.storage.NoSuchTrendBarException;
import com.myworktech.trendbar.service.storage.StorageFacade;
import com.myworktech.trendbar.service.storage.TrendBarHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class DefaultTrendBarHistoryService implements TrendBarHistoryService {

    private final StorageFacade storageFacade;

    @Autowired
    public DefaultTrendBarHistoryService(@Qualifier("storageFacade") StorageFacade storageFacade) {
        this.storageFacade = storageFacade;
    }

    @Override
    public Set<CompletedTrendBar> buildTrendBarsHistory(Symbol symbol, TrendBarType trendBarType, LocalDateTime from) throws NoSuchTrendBarException {
        return buildTrendBarsHistory(symbol, trendBarType, from, LocalDateTime.now());
    }

    @Override
    public Set<CompletedTrendBar> buildTrendBarsHistory(Symbol symbol, TrendBarType trendBarType, LocalDateTime from, LocalDateTime to) throws NoSuchTrendBarException {
        return storageFacade.buildReport(symbol, trendBarType, from, to);
    }
}
