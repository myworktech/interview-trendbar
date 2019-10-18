package com.myworktech.trendbar.service;

import com.myworktech.trendbar.model.CompletedTrendBar;
import com.myworktech.trendbar.model.Quote;
import com.myworktech.trendbar.model.Symbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DefaultTrendBarService implements TrendBarService {


    private final QuoteHandlerService quoteHandlerService;

    @Autowired
    public DefaultTrendBarService(QuoteHandlerService quoteHandlerService) {
        this.quoteHandlerService = quoteHandlerService;
    }

    @Override
    public void addQuote(Quote quote, Callback callback) {
        quoteHandlerService.handle(quote, callback);
    }

    @Override
    public Set<CompletedTrendBar> buildTrendBarsHistory(Symbol symbol) {
        return null;
    }


}
