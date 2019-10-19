package com.myworktech.trendbar;

import com.myworktech.trendbar.model.QuoteHandlerType;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;
import com.myworktech.trendbar.service.QuoteProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Profile("unitTest")
public class DemoTestHandlersConfiguration {

    @Bean(name = "quoteHandlerTypeList")
    public List<QuoteHandlerType> quoteHandlerTypeList() {
        List<QuoteHandlerType> list = new ArrayList<>();

        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDJPY"), TrendBarType.S1));

        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDEUR"), TrendBarType.M1));
        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDEUR"), TrendBarType.S1));

        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.H1));
        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.M1));
        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.S1));

        return Collections.unmodifiableList(list);
    }

    @Bean(name = "quoteProviderList")
    public List<QuoteProvider> quoteProviderList(List<QuoteHandlerType> quoteHandlerTypeList) {
        return quoteHandlerTypeList
                .stream()
                .map(QuoteHandlerType::getSymbol)
                .distinct()
                .map(DemoQuoteProvider::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

}
