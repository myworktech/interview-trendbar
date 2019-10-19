package com.myworktech.trendbar.config;

import com.myworktech.trendbar.model.QuoteHandlerType;
import com.myworktech.trendbar.model.Symbol;
import com.myworktech.trendbar.model.TrendBarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("example_config")
public class HandlersConfiguration {

    /**
     * Surely, we can inject symbol and trend bar types values from external text- or xml- based config for production use.
     * This config given as an example. There is a separate config for tests with another Spring profile.
     *
     * @return list of available trend bar types and symbols.
     */
    @Bean(name = "quoteHandlerTypeList")
    public List<QuoteHandlerType> quoteHandlerTypeList() {
        List<QuoteHandlerType> list = new ArrayList<>();

        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.M1));
        list.add(QuoteHandlerType.getInstance(Symbol.getInstance("USDRUB"), TrendBarType.S1));

        return list;
    }
}
