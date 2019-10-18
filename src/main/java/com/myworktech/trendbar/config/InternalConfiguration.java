package com.myworktech.trendbar.config;

import com.myworktech.trendbar.model.QuoteHandlerType;
import com.myworktech.trendbar.service.quoteHandler.DefaultQuoteHandlerService;
import com.myworktech.trendbar.service.quoteHandler.QuoteHandlerService;
import com.myworktech.trendbar.service.storage.CompletedTrendBarStorage;
import com.myworktech.trendbar.service.storage.StorageFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Profile("default")
public class InternalConfiguration {


    @Bean(name = "handlersMap")
    public Map<QuoteHandlerType, QuoteHandlerService> handlersMap(List<QuoteHandlerType> quoteHandlerTypeList) {
        Map<QuoteHandlerType, QuoteHandlerService> handlers = new HashMap<>();

        for (QuoteHandlerType quoteHandlerType : quoteHandlerTypeList) {
            handlers.put(quoteHandlerType, new DefaultQuoteHandlerService(new CompletedTrendBarStorage(quoteHandlerType), quoteHandlerType));
        }

        return handlers;
    }

    @Bean(name = "storageFacade")
    public StorageFacade storageFacade(Map<QuoteHandlerType, QuoteHandlerService> handlersMap) {
        List<CompletedTrendBarStorage> list = handlersMap.values().stream().map(QuoteHandlerService::getStorage).collect(Collectors.toList());

        return new StorageFacade(list);
    }
}
