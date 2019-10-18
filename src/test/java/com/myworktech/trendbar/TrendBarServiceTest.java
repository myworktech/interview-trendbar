package com.myworktech.trendbar;

import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.service.QuoteProvider;
import com.myworktech.trendbar.service.storage.StorageFacade;
import com.myworktech.trendbar.service.trendbar.TrendBarService;
import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
@ActiveProfiles({"unitTest", "default"})
@Log4j
public class TrendBarServiceTest {
    private static final int THREAD_COUNT = 50;
    private static final int CYCLE_COUNT_PER_THREAD = 4;
    private static final int TOTAL_CYCLE_COUNT =  THREAD_COUNT * (CYCLE_COUNT_PER_THREAD / 2) + THREAD_COUNT * CYCLE_COUNT_PER_THREAD;

    @Autowired
    private TrendBarService trendBarService;

    @Autowired
    private StorageFacade storageFacade;


    private final CountDownLatch countDownLatch = new CountDownLatch(TOTAL_CYCLE_COUNT);

    private QuoteProvider quoteProviderUSDRUB = new TestQuoteProvider2(Symbol.getInstance("USDRUB"));
    private QuoteProvider quoteProviderUSDEUR = new TestQuoteProvider2(Symbol.getInstance("USDEUR"));

    private final Random random = new Random();


    @Test
    public void test1() throws Throwable {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < CYCLE_COUNT_PER_THREAD; j++) {

                    Quote q = quoteProviderUSDRUB.getQuote();

                    trendBarService.addQuote(q, countDownLatch::countDown);
                    try {
                        Thread.sleep(random.nextInt(500));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            thread.start();
        }
        countDownLatch.await();
        trendBarService.shutdownService();

        Assert.assertEquals(TOTAL_CYCLE_COUNT, storageFacade.getSuperTotal());
    }
}
