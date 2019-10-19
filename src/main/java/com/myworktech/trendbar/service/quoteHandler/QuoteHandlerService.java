package com.myworktech.trendbar.service.quoteHandler;

import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.service.Callback;
import com.myworktech.trendbar.service.storage.CompletedTrendBarStorage;

/**
 * It is possible to get RejectedExecutionException, if call {@link #handle(Quote, Callback)} when someone calls {@link #shutdownService()} method.
 * It's better to catch the exception and return {@code boolean} from the handle() method
 * to inform the client if a {@code quote} hasn't been accepted to process.
 */
public interface QuoteHandlerService {
    void handle(Quote quote, Callback callback);

    void shutdownService() throws InterruptedException;

    CompletedTrendBarStorage getStorage();
}
