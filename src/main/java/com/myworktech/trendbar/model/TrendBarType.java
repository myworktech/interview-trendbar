package com.myworktech.trendbar.model;

import lombok.Getter;

import java.time.temporal.ChronoField;

public enum TrendBarType {
    S1(ChronoField.SECOND_OF_MINUTE), M1(ChronoField.MINUTE_OF_HOUR), H1(ChronoField.HOUR_OF_DAY), D1(ChronoField.DAY_OF_MONTH);

    @Getter
    private final ChronoField chronoField;

    TrendBarType(ChronoField chronoField) {
        this.chronoField = chronoField;
    }
}
