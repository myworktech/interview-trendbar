package com.myworktech.trendbar.service;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.CurrentTrendBar;
import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.TrendBarType;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j
public class QuoteHandlerService {

    private final ExecutorService executorService;
    private final CompletedTrendBarStorage storage;


    @Getter
    private volatile CurrentTrendBar currentTrendBar;


    @Autowired
    public QuoteHandlerService(CompletedTrendBarStorage storage) {
        this.storage = storage;
        this.executorService = Executors.newScheduledThreadPool(10);

    }

    public void handle(Quote quote, Callback callback) {
        executorService.submit(new QuoteHandler(quote, callback));
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
                log.debug("Start processing quote: " + quote);
                if (currentTrendBar == null)
                    synchronized (QuoteHandlerService.this) {
                        if (currentTrendBar == null) {
                            currentTrendBar = new CurrentTrendBar(TrendBarType.M1, quote);
                        } else {
                            currentTrendBar.addQuote(quote);
                        }
                    }
                else if (quote.getTimeStamp().getSecond() > currentTrendBar.getLastQuote().getTimeStamp().getSecond()) {
                    CurrentTrendBar old = currentTrendBar;
                    synchronized (QuoteHandlerService.this) {
                        if (old == currentTrendBar) {
                            storage.add(new CompletedTrendBar(currentTrendBar));
                            currentTrendBar = new CurrentTrendBar(TrendBarType.M1, quote);
                        } else
                            currentTrendBar.addQuote(quote);
                    }
                } else
                    currentTrendBar.addQuote(quote);
            } catch (RuntimeException e) {
                log.error("Error handling quote" + e.getMessage(), e);
            } finally {
                log.debug("Finish processing quote: " + quote);
                callback.action();
            }
        }
    }
}
