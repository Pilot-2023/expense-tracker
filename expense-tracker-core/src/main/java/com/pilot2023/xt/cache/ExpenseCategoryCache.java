package com.pilot2023.xt.cache;

import com.pilot2023.xt.domain.ExpenseCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class ExpenseCategoryCache {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private static Map<String, ExpenseCategory> categories;

}
