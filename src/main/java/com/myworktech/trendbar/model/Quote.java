package com.myworktech.trendbar.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class Quote {
    private final Symbol symbol;
    private final long price;
    private final LocalDateTime timeStamp;
}
