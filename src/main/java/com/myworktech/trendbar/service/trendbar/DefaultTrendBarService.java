package com.myworktech.trendbar.service.trendbar;

import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.QuoteHandlerType;
import com.myworktech.trendbar.service.Callback;
import com.myworktech.trendbar.service.quoteHandler.QuoteHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultTrendBarService implements TrendBarService {

    private final Map<QuoteHandlerType, QuoteHandlerService> handlers;

    @Autowired
    public DefaultTrendBarService(@Qualifier("handlersMap") Map<QuoteHandlerType, QuoteHandlerService> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void addQuote(Quote quote, Callback callback) {
        for (QuoteHandlerType quoteHandlerType : handlers.keySet()) {
            if (quoteHandlerType.getSymbol().equals(quote.getSymbol()))
                handlers.get(quoteHandlerType).handle(quote, callback);
        }
    }

    @Override
    public void shutdownService() throws InterruptedException {
        for (QuoteHandlerService quoteHandlerService : handlers.values()) {
            quoteHandlerService.shutdownService();
        }
    }
}
