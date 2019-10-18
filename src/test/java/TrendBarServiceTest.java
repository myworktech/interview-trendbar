import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.service.CompletedTrendBarStorage;
import com.myworktech.trendbar.service.TrendBarService;
import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
@Log4j
public class TrendBarServiceTest {
    @Autowired
    private TrendBarService trendBarService;
    @Autowired
    private CompletedTrendBarStorage storage;

    private final CountDownLatch countDownLatch = new CountDownLatch(200);

    private TestQuoteProvider quoteProvider = new TestQuoteProvider();

    private final Random random = new Random();


    @Test
    public void test1() throws Throwable {
        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 4; j++) {
                    Quote q = quoteProvider.getQuote();
                    trendBarService.addQuote(q, countDownLatch::countDown);
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            thread.start();

        }
        countDownLatch.await();
        trendBarService.shutdownService();

        CompletedTrendBar[] l = storage.getAll();

        log.info(Arrays.toString(l).replaceAll("},", "),\n"));


        Assert.assertEquals(200, (int) Stream.of(l).map(CompletedTrendBar::getQuotesCount).reduce(0, Integer::sum));

    }
}
