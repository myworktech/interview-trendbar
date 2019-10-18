package com.myworktech.trendbar.service;

import com.myworktech.trendbar.model.CompletedTrendBar;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Log4j
public class CompletedTrendBarStorage {

    private final Queue<CompletedTrendBar> list = new ConcurrentLinkedQueue<>();


    public void add(CompletedTrendBar completedTrendBar) {
        list.add(completedTrendBar);
        log.info("Added to storage CompletedTrendBar: " + completedTrendBar.getOpenPrice() + " " + completedTrendBar.getClosePrice());
    }

    public CompletedTrendBar[] getAll() {
       return list.toArray(new CompletedTrendBar[]{});
    }
}
