package com.pilot2023.xt.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum Direction {
    ASC("asc"),
    DESC("desc");

    private final String value;
}
