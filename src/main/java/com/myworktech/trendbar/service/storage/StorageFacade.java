package com.myworktech.trendbar.service.storage;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.QuoteHandlerType;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
public class StorageFacade {
    private final List<CompletedTrendBarStorage> list;

    public StorageFacade(List<CompletedTrendBarStorage> list) {
        this.list = list;
    }

    public int getTrendBarCountByAllStorages() {
        return list
                .stream()
                .map(CompletedTrendBarStorage::getAllStoredItems)
                .flatMap(Arrays::stream)
                .map(CompletedTrendBar::getQuotesCount)
                .reduce(0, Integer::sum);
    }

    public Set<CompletedTrendBar> buildReport(Symbol symbol, TrendBarType trendBarType, LocalDateTime from, LocalDateTime to) throws NoSuchTrendBarException {
        QuoteHandlerType typeToSearch = QuoteHandlerType.getInstance(symbol, trendBarType);
        CompletedTrendBarStorage storage = list
                .stream()
                .filter(s -> s.getQuoteHandlerType().equals(typeToSearch))
                .findFirst()
                .orElseThrow(() -> new NoSuchTrendBarException(typeToSearch));

        return storage.searchByDates(from, to);
    }
}
