import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.service.QuoteHandlerService;
import com.myworktech.trendbar.service.TrendBarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class TrendBarServiceTest {

    @Autowired
    private TrendBarService trendBarService;

    private final CountDownLatch countDownLatch = new CountDownLatch(200);


    private TestQuoteProvider quoteProvider = new TestQuoteProvider();

    private Random random = new Random();

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

//        CompletedTrendBar[] l = trendBarService.getStorage().getAll();

//        System.out.println(Arrays.toString(l).replaceAll("},", "),\n"));
//        CurrentTrendBar last = trendBarService.getCurrentTrendBar();

//        System.out.println("Last: closePrice=" + last.getClosePrice() + ", openPrice= " + last.getOpenPrice() + ", quotesCount=" + last.getQuoteSet().size());
//        Assert.assertEquals(200, Stream.of(l).map(CompletedTrendBar::getQuotesCount).reduce(0, Integer::sum) + last.getQuoteSet().size());

    }
}
