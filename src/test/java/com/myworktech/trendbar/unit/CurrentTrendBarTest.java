package com.myworktech.trendbar.unit;

import com.myworktech.trendbar.model.CurrentTrendBar;
import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class CurrentTrendBarTest {


    @Test
    public void testCurrentTrendBar() throws Throwable {
        final long openPrice = 10L;
        final long closePrice = 20L;
        final long lowPrice = 2L;
        final long highPrice = 50L;

        Symbol symbol = Symbol.getInstance("RUBUSD");

        LocalDateTime openTimeStamp = LocalDateTime.of(2019, 10, 1, 10, 10, 10);
        LocalDateTime inTheMiddle1 = openTimeStamp.plusSeconds(5);
        LocalDateTime inTheMiddle2 = openTimeStamp.plusSeconds(10);
        LocalDateTime closeTimeStamp = openTimeStamp.plusSeconds(30);

        Quote openQuote = new Quote(symbol, openPrice, openTimeStamp);
        CurrentTrendBar currentTrendBar = new CurrentTrendBar(TrendBarType.M1, openQuote);

        currentTrendBar.addQuote(new Quote(symbol, closePrice, closeTimeStamp));
        currentTrendBar.addQuote(new Quote(symbol, lowPrice, inTheMiddle1));
        currentTrendBar.addQuote(new Quote(symbol, highPrice, inTheMiddle2));

        Assert.assertEquals(currentTrendBar.getClosePrice(), closePrice);
        Assert.assertEquals(currentTrendBar.getOpenPrice(), openPrice);
        Assert.assertEquals(currentTrendBar.getHighPrice(), highPrice);
        Assert.assertEquals(currentTrendBar.getLowPrice(), lowPrice);
    }

    @Test
    public void testCurrentTrendBarConcurrent() throws Throwable {
        final int DELAY_MILLIS = 100;
        final int THREAD_COUNT = 50;
        final int CYCLE_COUNT_PER_THREAD = 40;
        final int TOTAL_CYCLE_COUNT = THREAD_COUNT * CYCLE_COUNT_PER_THREAD;

        final long openPrice = 10L;
        final long closePrice = 20L;
        final long lowPrice = 2L;
        final long highPrice = 50L;

        final CountDownLatch countDownLatch = new CountDownLatch(TOTAL_CYCLE_COUNT);

        Symbol symbol = Symbol.getInstance("RUBUSD");

        LocalDateTime openTimeStamp = LocalDateTime.of(2019, 10, 1, 10, 10, 10);
        LocalDateTime inTheMiddle1 = openTimeStamp.plusSeconds(5);
        LocalDateTime inTheMiddle2 = openTimeStamp.plusSeconds(10);
        LocalDateTime closeTimeStamp = openTimeStamp.plusSeconds(30);

        Quote openQuote = new Quote(symbol, openPrice, openTimeStamp);
        CurrentTrendBar currentTrendBar = new CurrentTrendBar(TrendBarType.M1, openQuote);

        currentTrendBar.addQuote(new Quote(symbol, closePrice, closeTimeStamp));
        currentTrendBar.addQuote(new Quote(symbol, lowPrice, inTheMiddle1));
        currentTrendBar.addQuote(new Quote(symbol, highPrice, inTheMiddle2));

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < CYCLE_COUNT_PER_THREAD; j++) {
                    long nextRandomPrice = ThreadLocalRandom.current().nextLong(highPrice - lowPrice - 1) + lowPrice + 1;
                    LocalDateTime nextRandomTimeStamp = openTimeStamp.plusSeconds(ThreadLocalRandom.current().nextLong(20) + 1);
                    currentTrendBar.addQuote(new Quote(symbol, nextRandomPrice, nextRandomTimeStamp));
                    countDownLatch.countDown();
                    try {
                        Thread.sleep(DELAY_MILLIS);
                    } catch (InterruptedException ignored) {
                    }
                }
            });
            thread.start();
        }

        countDownLatch.await();

        final int EXPLICITLY_ADDED_QUOTES = 4;

        Assert.assertEquals(closePrice, currentTrendBar.getClosePrice());
        Assert.assertEquals(openPrice, currentTrendBar.getOpenPrice());
        Assert.assertEquals(highPrice, currentTrendBar.getHighPrice());
        Assert.assertEquals(lowPrice, currentTrendBar.getLowPrice());
        Assert.assertEquals(TOTAL_CYCLE_COUNT + EXPLICITLY_ADDED_QUOTES, currentTrendBar.getQuoteSet().size());
    }
}
