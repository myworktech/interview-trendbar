package com.fxpro.trendbar;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CompletedTrendBarStorage {

    private final Queue<CompletedTrendBar> list = new ConcurrentLinkedQueue<>();


    public void add(CompletedTrendBar completedTrendBar) {
        list.add(completedTrendBar);
        System.out.println("added to storage " + completedTrendBar.getOpenPrice() + " " + completedTrendBar.getClosePrice());
    }

    public void printAll() {
        System.out.println(Arrays.toString(list.toArray()).replaceAll("\\},", "),\n"));
    }
}
