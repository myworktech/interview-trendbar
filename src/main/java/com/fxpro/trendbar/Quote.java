package com.fxpro.trendbar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Quote {
    private final Symbol symbol;
    private final long price;
    private final LocalDateTime timeStamp;
}
