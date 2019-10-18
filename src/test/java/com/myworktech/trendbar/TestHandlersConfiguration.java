package com.myworktech.trendbar;

import com.myworktech.trendbar.model.QuoteHandlerType;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("unitTest")
public class TestHandlersConfiguration {

    /**
     * Surely, we can inject these values from external text- or xml- based config.
     * @return list of available trend bar types and symbols.
     */
    @Bean(name = "quoteHandlerTypeList")
    public List<QuoteHandlerType> quoteHandlerTypeList() {
        List<QuoteHandlerType> list = new ArrayList<>();

//        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDEUR"), TrendBarType.M1));
        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDEUR"), TrendBarType.S1));

//        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.H1));
        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.M1));
        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.S1));

        return list;
    }


}
