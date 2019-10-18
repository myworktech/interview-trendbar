package com.myworktech.trendbar.service.storage;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.QuoteHandlerType;
import lombok.extern.log4j.Log4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log4j
public class CompletedTrendBarStorage {

    private final Queue<CompletedTrendBar> list = new ConcurrentLinkedQueue<>();
    private final QuoteHandlerType quoteHandlerType;

    public CompletedTrendBarStorage(QuoteHandlerType quoteHandlerType) {
        this.quoteHandlerType = quoteHandlerType;
    }

    public void add(CompletedTrendBar completedTrendBar) {
        list.add(completedTrendBar);
        log.info("Storage " + quoteHandlerType + " saved: " + completedTrendBar.getTrendBarType().name() + " " + completedTrendBar.getLowPrice() + " " + completedTrendBar.getHighPrice());
    }

    public CompletedTrendBar[] getAll() {
        return list.toArray(new CompletedTrendBar[]{});
    }
}
