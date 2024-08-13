package com.pilot2023.xt.dataprovider;

import com.pilot2023.xt.domain.ExpenseCategory;

import java.util.List;

public interface ExpenseCategoryDataProviderPersistence {

    List<ExpenseCategory> getAll();

    void saveAll(List<ExpenseCategory> categories);

    void deleteAll();

}