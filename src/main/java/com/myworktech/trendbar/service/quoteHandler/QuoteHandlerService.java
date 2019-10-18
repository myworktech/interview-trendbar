package com.myworktech.trendbar.service.quoteHandler;

import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.service.Callback;
import com.myworktech.trendbar.service.storage.CompletedTrendBarStorage;

public interface QuoteHandlerService {
    void handle(Quote quote, Callback callback);

    void shutdownService() throws InterruptedException;

    CompletedTrendBarStorage getStorage();


}
