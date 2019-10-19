package com.myworktech.trendbar.service.trendbar;

import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.service.Callback;

public interface TrendBarService {
    void addQuote(Quote quote, Callback callback);

    void shutdownService() throws InterruptedException;
}
