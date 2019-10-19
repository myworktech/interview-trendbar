package com.myworktech.trendbar.demo;

import com.myworktech.trendbar.model.*;
import com.myworktech.trendbar.service.QuoteProvider;
import com.myworktech.trendbar.service.storage.NoSuchTrendBarException;
import com.myworktech.trendbar.service.storage.StorageFacade;
import com.myworktech.trendbar.service.storage.TrendBarHistoryService;
import com.myworktech.trendbar.service.trendbar.TrendBarService;
import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
@ActiveProfiles({"demoTest", "default"})
@Log4j
public class TrendBarServiceTest {
    private static final int DELAY_MILLIS = 100;
    private static final int THREAD_COUNT = 50;
    private static final int CYCLE_COUNT_PER_THREAD = 40;
    private static final int TOTAL_CYCLE_COUNT = THREAD_COUNT * CYCLE_COUNT_PER_THREAD;
    private final Random random = new Random();
    @Autowired
    private TrendBarService trendBarService;
    @Autowired
    private StorageFacade storageFacade;
    @Autowired
    private List<QuoteProvider> quoteProviderList;
    @Autowired
    private List<QuoteHandlerType> quoteHandlerTypeList;
    @Autowired
    private TrendBarHistoryService trendBarHistoryService;

    @Test
    public void demoTest() throws Throwable {
        int SUPER_TOTAL_CYCLE_COUNT = TOTAL_CYCLE_COUNT * quoteHandlerTypeList.size();
        final CountDownLatch countDownLatch = new CountDownLatch(SUPER_TOTAL_CYCLE_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < CYCLE_COUNT_PER_THREAD; j++) {
                    for (QuoteProvider quoteProvider : quoteProviderList) {
                        Quote q = quoteProvider.getQuote();
                        trendBarService.addQuote(q, countDownLatch::countDown);
                        try {
                            Thread.sleep(random.nextInt(DELAY_MILLIS));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

            });
            thread.start();
        }
        countDownLatch.await();
        trendBarService.shutdownService();

        Assert.assertEquals(SUPER_TOTAL_CYCLE_COUNT, storageFacade.getTrendBarCountByAllStorages());

        try {
            trendBarHistoryService.buildTrendBarsHistory(Symbol.getInstance("AUDUSD"), TrendBarType.M1, LocalDateTime.now());
        } catch (NoSuchTrendBarException e) {
            Assert.assertNotNull(e);
        }

        Set<CompletedTrendBar> historySet = trendBarHistoryService.buildTrendBarsHistory(Symbol.getInstance("USDRUB"), TrendBarType.S1, LocalDateTime.now().minusHours(1L));

        log.info(historySet.toString());
    }
}
