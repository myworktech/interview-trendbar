import com.fxpro.trendbar.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class TrendBarServiceTest {


    private TrendBarServiceImpl trendBarService = new TrendBarServiceImpl();

    private QuoteProvider quoteProvider = new TestQuoteProvider();
    private Random random = new Random();

    @Test
    public void test1() throws Throwable {
        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 4; j++) {
                    Quote q = quoteProvider.getQuote();
                    trendBarService.addQuote(q);
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            thread.start();

        }
        trendBarService.getCountDownLatch().await();

        CompletedTrendBar[] l = trendBarService.getStorage().getAll();

        System.out.println(Arrays.toString(l).replaceAll("},", "),\n"));
        CurrentTrendBar last = trendBarService.getCurrentTrendBar();

        System.out.println("Last: closePrice=" + last.getClosePrice() + ", openPrice= " + last.getOpenPrice() + ", quotesCount=" + last.getQuoteSet().size());
        Assert.assertEquals(200, Stream.of(l).map(CompletedTrendBar::getQuotesCount).reduce(0, Integer::sum) + last.getQuoteSet().size());

    }
}
