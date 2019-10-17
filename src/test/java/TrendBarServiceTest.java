import com.fxpro.trendbar.*;
import org.junit.Test;

import java.util.Random;

public class TrendBarServiceTest {


    private TrendBarServiceImpl trendBarService = new TrendBarServiceImpl();

    private QuoteProvider quoteProvider = new TestQuoteProvider();
    private Random random = new Random();

    @Test
    public void test1() throws Throwable {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 40; j++) {
                    Quote q = quoteProvider.getQuote();
                    trendBarService.addQuote(q);
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            thread.start();

        }
        trendBarService.getCountDownLatch().await();

        trendBarService.getStorage().printAll();
        CurrentTrendBar last = trendBarService.getCurrentTrendBar();

        System.out.println("Last: closePrice=" + last.getClosePrice() + ", openPrice= " + last.getOpenPrice() + ", quotesCount=" + last.getQuoteSet().size());
        System.out.println(1);
    }
}
