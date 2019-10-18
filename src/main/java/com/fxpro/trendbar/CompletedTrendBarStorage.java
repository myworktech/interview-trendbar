package com.fxpro.trendbar;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CompletedTrendBarStorage {

    private final Queue<CompletedTrendBar> list = new ConcurrentLinkedQueue<>();


    public void add(CompletedTrendBar completedTrendBar) {
        list.add(completedTrendBar);
        System.out.println("added to storage " + completedTrendBar.getOpenPrice() + " " + completedTrendBar.getClosePrice());
    }

    public CompletedTrendBar[] getAll() {
       return list.toArray(new CompletedTrendBar[]{});
    }
}
