package com.fxpro.trendbar;

import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class TrendBarServiceImpl implements TrendBarService {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    private volatile CurrentTrendBar currentTrendBar;
    @Getter
    private final CompletedTrendBarStorage storage = new CompletedTrendBarStorage();
    @Getter
    private final CountDownLatch countDownLatch = new CountDownLatch(200);

    private final ReentrantLock lock = new ReentrantLock();


    @Override
    public void addQuote(Quote quote) {
        executorService.submit(new R(quote));
    }

    @Override
    public Set<TrendBar> buildTrendBarsHistory(Symbol symbol) {
        return null;
    }


    private class R implements Runnable {

        private final Quote quote;

        public R(Quote quote) {
            this.quote = quote;
        }

        @Override
        public void run() {
            try {
                System.out.println("Start processing quote: " + quote.getPrice() + " " + quote.getTimeStamp().getSecond());
                if (currentTrendBar == null)
                    try {
                        lock.lock();
//                    synchronized (this)

                        if (currentTrendBar == null)
                            currentTrendBar = new CurrentTrendBar(TrendBarType.M1, quote);
                    } finally {
                        lock.unlock();
                    }
                else if (quote.getTimeStamp().getSecond() > currentTrendBar.getLastQuote().getTimeStamp().getSecond()) {
                    boolean res = false;

                    try {
                        if ((res = lock.tryLock())) {
//                    synchronized (this)
//                        if ( quote.getTimeStamp().getSecond() > currentTrendBar.getLastQuote().getTimeStamp().getSecond()) {
                            storage.add(new CompletedTrendBar(currentTrendBar));
                            currentTrendBar = new CurrentTrendBar(TrendBarType.M1, quote);
//                        } else {

                        } else
                            currentTrendBar.addQuote(quote);
                    } finally {
                        if (res)
                            lock.unlock();
                    }


                } else
                    currentTrendBar.addQuote(quote);
            } catch (RuntimeException e) {
                System.out.println("error" + e.getMessage());
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }

        }


    }
}
