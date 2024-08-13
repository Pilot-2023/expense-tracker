package com.pilot2023.xt.util;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class Util {

    private Util() {
        throw new IllegalStateException("Cannot instantiate a Utils class");
    }

    public static boolean inputFieldGiven(Object field) {
        return Objects.nonNull(field);
    }

    public static boolean inputFieldFilled(String field) {
        return StringUtils.hasText(field);
    }

}
