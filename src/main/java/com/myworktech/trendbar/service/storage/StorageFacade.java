package com.myworktech.trendbar.service.storage;

import com.myworktech.trendbar.model.CompletedTrendBar;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class StorageFacade {
    private final List<CompletedTrendBarStorage> list;

    public StorageFacade(List<CompletedTrendBarStorage> list) {
        this.list = list;
    }

    public int getSuperTotal() {
        return list
                .stream()
                .map(CompletedTrendBarStorage::getAll)
                .flatMap(Arrays::stream)
                .map(CompletedTrendBar::getQuotesCount)
                .reduce(0, Integer::sum);
    }
}
