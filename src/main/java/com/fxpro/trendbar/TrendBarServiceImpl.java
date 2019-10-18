package com.fxpro.trendbar;

import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrendBarServiceImpl implements TrendBarService {

    private final ExecutorService executorService = Executors.newScheduledThreadPool(10);
    @Getter
    private volatile CurrentTrendBar currentTrendBar;
    @Getter
    private final CompletedTrendBarStorage storage = new CompletedTrendBarStorage();
    @Getter
    private final CountDownLatch countDownLatch = new CountDownLatch(200);

    @Override
    public void addQuote(Quote quote) {
        executorService.submit(new QuoteHandler(quote));
    }

    @Override
    public Set<TrendBar> buildTrendBarsHistory(Symbol symbol) {
        return null;
    }


    private class QuoteHandler implements Runnable {

        private final Quote quote;

        public QuoteHandler(Quote quote) {
            this.quote = quote;
        }


        @Override
        public void run() {
            try {
                System.out.println("Start processing quote: " + quote.getPrice() + " " + quote.getTimeStamp().getSecond());
                if (currentTrendBar == null)
                    synchronized (TrendBarServiceImpl.this) {
                        if (currentTrendBar == null) {
                            currentTrendBar = new CurrentTrendBar(TrendBarType.M1, quote);
                        } else {
                            currentTrendBar.addQuote(quote);
                        }
                    }
                else if (quote.getTimeStamp().getSecond() > currentTrendBar.getLastQuote().getTimeStamp().getSecond()) {
                    CurrentTrendBar old = currentTrendBar;
                    synchronized (TrendBarServiceImpl.this) {
                        if (old == currentTrendBar) {
                            storage.add(new CompletedTrendBar(currentTrendBar));
                            currentTrendBar = new CurrentTrendBar(TrendBarType.M1, quote);
                        } else
                            currentTrendBar.addQuote(quote);
                    }
                } else
                    currentTrendBar.addQuote(quote);
            } catch (RuntimeException e) {
                System.out.println("Error handling quote" + e.getMessage());
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
