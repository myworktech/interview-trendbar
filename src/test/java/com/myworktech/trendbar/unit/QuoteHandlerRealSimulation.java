package com.myworktech.trendbar.unit;

import com.myworktech.trendbar.model.*;
import com.myworktech.trendbar.service.quoteHandler.DefaultQuoteHandlerService;
import com.myworktech.trendbar.service.storage.CompletedTrendBarStorage;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

public class QuoteHandlerRealSimulation {

    private final LocalDateTime startDateTime = LocalDateTime.of(2019, 10, 1, 1, 1, 1);
    private final Symbol symbol = Symbol.getInstance("RUBUSD");

    @Test
    public void testRealSimulation() throws Throwable {
        QuoteHandlerType quoteHandlerType = QuoteHandlerType.getInstance(symbol, TrendBarType.S1);

        CompletedTrendBarStorage storage = new CompletedTrendBarStorage(quoteHandlerType);

        DefaultQuoteHandlerService service = new DefaultQuoteHandlerService(storage, quoteHandlerType);

        CountDownLatch countDownLatch = new CountDownLatch(5);

        service.handle(new Quote(symbol, 10L, startDateTime), countDownLatch::countDown);
        Thread.sleep(2000);
        service.handle(new Quote(symbol, 1L, startDateTime.plusSeconds(2)), countDownLatch::countDown);
        Thread.sleep(1000);
        service.handle(new Quote(symbol, 100L, startDateTime.plusSeconds(3)), countDownLatch::countDown);
        service.handle(new Quote(symbol, 50L, startDateTime.plusSeconds(3)), countDownLatch::countDown);
        service.handle(new Quote(symbol, 20L, startDateTime.plusSeconds(3)), countDownLatch::countDown);
        Thread.sleep(1000);
        service.handle(new Quote(symbol, 2L, startDateTime.plusSeconds(4)), countDownLatch::countDown);
        service.handle(new Quote(symbol, 200L, startDateTime.plusSeconds(4)), countDownLatch::countDown);
        service.handle(new Quote(symbol, 52L, startDateTime.plusSeconds(4)), countDownLatch::countDown);
        service.handle(new Quote(symbol, 666L, startDateTime.plusSeconds(4)), countDownLatch::countDown);
        service.handle(new Quote(symbol, 777, startDateTime.plusSeconds(4)), countDownLatch::countDown);

        countDownLatch.await();

        service.shutdownService();

        CompletedTrendBar[] s = storage.getAllStoredItems();
        Assert.assertEquals(4, s.length); // 4 completed TBs
        Assert.assertEquals(777, s[3].getHighPrice());
        Assert.assertEquals(2, s[3].getLowPrice());
    }
}
