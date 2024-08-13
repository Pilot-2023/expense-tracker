package com.pilot2023.xt.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum OrderBy {
    COST("cost"),
    DATE("date");

    private final String value;
}
