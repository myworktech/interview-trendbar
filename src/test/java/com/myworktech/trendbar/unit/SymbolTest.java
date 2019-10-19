package com.myworktech.trendbar.unit;

import com.myworktech.trendbar.model.Symbol;
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

public class SymbolTest {

    @Test
    public void testSameInstance() throws Throwable {
        Symbol s1 = Symbol.getInstance("USDRUB");
        Symbol s2 = Symbol.getInstance("USDRUB");
        Assert.assertTrue(s1 == s2);
    }

    @Test
    public void testMirroredPairs() throws Throwable {
        Symbol s1 = Symbol.getInstance("RUBUSD");
        Symbol s2 = Symbol.getInstance("USDRUB");
        Assert.assertTrue(s1 != s2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImpossiblePair() throws Throwable {
        Symbol.getInstance("RUBRUB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCurrency() throws Throwable {
        Symbol.getInstance("123");
    }

    @Test
    public void testSameInstanceDifferentThreads() throws Throwable {
        Callable<Symbol> callable = () -> Symbol.getInstance("RUBUSD");
        ExecutorService ex = Executors.newFixedThreadPool(20);
        List<Future<Symbol>> futureList = ex.invokeAll(Arrays.asList(callable, callable, callable, callable));

        long count = 0L;
        Set<Symbol> uniqueValues = new HashSet<>();
        for (Future<Symbol> symbolFuture : futureList) {
            Symbol symbol = symbolFuture.get();
            if (uniqueValues.add(symbol)) {
                count++;
            }
        }
        Assert.assertEquals(1, count);
    }
}
