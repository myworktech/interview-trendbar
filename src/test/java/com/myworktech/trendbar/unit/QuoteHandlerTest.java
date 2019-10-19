package com.myworktech.trendbar.unit;

import com.myworktech.trendbar.model.QuoteHandlerType;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QuoteHandlerTest {

    @Test
    public void testSameInstance() throws Throwable {
        Symbol symbol = Symbol.getInstance("USDRUB");

        QuoteHandlerType q1 = QuoteHandlerType.getInstance(symbol, TrendBarType.M1);
        QuoteHandlerType q2 = QuoteHandlerType.getInstance(symbol, TrendBarType.M1);
        Assert.assertTrue(q1 == q2);
    }

    @Test
    public void testDifferentTBTypes1() throws Throwable {
        Symbol symbol = Symbol.getInstance("USDRUB");

        QuoteHandlerType q1 = QuoteHandlerType.getInstance(symbol, TrendBarType.M1);
        QuoteHandlerType q2 = QuoteHandlerType.getInstance(symbol, TrendBarType.S1);
        Assert.assertTrue(q1 != q2);
    }

    @Test
    public void testDifferentTBTypes2() throws Throwable {
        Symbol symbol1 = Symbol.getInstance("USDRUB");
        Symbol symbol2 = Symbol.getInstance("USDEUR");
        Assert.assertTrue(symbol1 != symbol2);


        QuoteHandlerType q1 = QuoteHandlerType.getInstance(symbol1, TrendBarType.S1);
        QuoteHandlerType q2 = QuoteHandlerType.getInstance(symbol2, TrendBarType.S1);
        Assert.assertTrue(q1 != q2);
    }

    @Test
    public void testSameInstanceDifferentThreads() throws Throwable {
        Callable<QuoteHandlerType> callable = () -> QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.S1);
        ExecutorService ex = Executors.newFixedThreadPool(20);
        List<Future<QuoteHandlerType>> futureList = ex.invokeAll(Arrays.asList(callable, callable, callable, callable));

        long count = 0L;
        Set<QuoteHandlerType> uniqueValues = new HashSet<>();
        for (Future<QuoteHandlerType> handlerTypeFuture : futureList) {
            QuoteHandlerType QuoteHandlerType = handlerTypeFuture.get();
            if (uniqueValues.add(QuoteHandlerType)) {
                count++;
            }
        }
        Assert.assertEquals(1, count);
    }
}
