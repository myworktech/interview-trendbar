package com.myworktech.trendbar.service.storage;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.QuoteHandlerType;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Concurrent insertions are very rare here, so we may consider use non-concurrent tree-based collection here (e.g. TreeMap) and guard it with a synchronized block or a ReadWriteLock.
 */
@Log4j
public class CompletedTrendBarStorage {

    private final SortedSet<CompletedTrendBar> list = new ConcurrentSkipListSet<>(Comparator.comparing(CompletedTrendBar::getCreatedTimeStamp));
    @Getter
    private final QuoteHandlerType quoteHandlerType;

    public CompletedTrendBarStorage(QuoteHandlerType quoteHandlerType) {
        this.quoteHandlerType = quoteHandlerType;
    }

    public void add(CompletedTrendBar completedTrendBar) {
        list.add(completedTrendBar);
        log.info("Storage for " + quoteHandlerType.toString() + " has saved a quote: " + completedTrendBar.getLowPrice() + " " + completedTrendBar.getHighPrice());
    }

    public CompletedTrendBar[] getAllStoredItems() {
        return list.toArray(new CompletedTrendBar[]{});
    }

    public Set<CompletedTrendBar> searchByDates(LocalDateTime from, LocalDateTime to) {
        Iterator<CompletedTrendBar> it = list.iterator();

        Set<CompletedTrendBar> completedTrendBars = new LinkedHashSet<>();

        while (it.hasNext()) {
            CompletedTrendBar next = it.next();

            if (from.isBefore(next.getCreatedTimeStamp()) && to.isAfter(next.getCreatedTimeStamp()))
                completedTrendBars.add(next);
        }
        return completedTrendBars;

    }
}
