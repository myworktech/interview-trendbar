package com.myworktech.trendbar.service.quoteHandler;

import com.myworktech.trendbar.model.*;
import com.myworktech.trendbar.service.Callback;
import com.myworktech.trendbar.service.storage.CompletedTrendBarStorage;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.time.temporal.ChronoField;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j
public class DefaultQuoteHandlerService implements QuoteHandlerService {

    private final ExecutorService executorService;
    @Getter
    private final CompletedTrendBarStorage storage;
    @Getter
    private final TrendBarType trendBarType;
    @Getter
    private final Symbol symbol;

    private volatile CurrentTrendBar currentTrendBar;

    public DefaultQuoteHandlerService(CompletedTrendBarStorage storage, QuoteHandlerType quoteHandlerType) {
        this.storage = storage;
        this.trendBarType = quoteHandlerType.getTrendBarType();
        this.symbol = quoteHandlerType.getSymbol();
        this.executorService = Executors.newCachedThreadPool(new CustomNameThreadFactory(quoteHandlerType.getTrendBarType().name()));
    }

    @Override
    public void handle(Quote quote, Callback callback) {
        executorService.submit(new QuoteHandler(quote, callback));
    }

    @Override
    public void shutdownService() throws InterruptedException {
        log.info("Shutting down service: " + trendBarType.name() + " " + symbol);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
        if (currentTrendBar != null)
            storage.add(new CompletedTrendBar(currentTrendBar));
    }

    private boolean checkIfPeriodIsOver(Quote quote1, Quote quote2) {
        ChronoField chronoField = trendBarType.getChronoField();
        return quote1.getTimeStamp().get(chronoField) != quote2.getTimeStamp().get(chronoField);
    }

    private class QuoteHandler implements Runnable {

        private final Quote quote;
        private final Callback callback;

        QuoteHandler(Quote quote, Callback callback) {
            this.quote = quote;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                log.debug("Start: " + quote);
                if (currentTrendBar == null)
                    synchronized (DefaultQuoteHandlerService.this) {
                        if (currentTrendBar == null) {
                            currentTrendBar = new CurrentTrendBar(trendBarType, quote);
                        } else {
                            currentTrendBar.addQuote(quote);
                        }
                    }
                else if (checkIfPeriodIsOver(quote, currentTrendBar.getLastQuote())) {
                    CurrentTrendBar old = currentTrendBar;
                    synchronized (DefaultQuoteHandlerService.this) {
                        if (old == currentTrendBar) {
                            storage.add(new CompletedTrendBar(currentTrendBar));
                            currentTrendBar = new CurrentTrendBar(trendBarType, quote);
                        } else
                            currentTrendBar.addQuote(quote);
                    }
                } else
                    currentTrendBar.addQuote(quote);
            } catch (RuntimeException e) {
                log.error("Error handling quote" + e.getMessage(), e);
            } finally {
                log.debug("Finish: " + quote);
                callback.finishProcessingQuote();
            }
        }
    }

}
