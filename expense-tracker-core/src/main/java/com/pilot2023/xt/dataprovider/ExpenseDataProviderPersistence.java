package com.pilot2023.xt.dataprovider;

import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.dto.ExpenseCreate;
import com.pilot2023.xt.dto.ExpenseUpdate;
import com.pilot2023.xt.usecases.ExpenseUseCaseList;

import java.util.List;

public interface ExpenseDataProviderPersistence {

    Expense create(ExpenseCreate expense);

    Expense getById(String id);

    List<Expense> list(ExpenseUseCaseList.Input input);

    Expense update(String id, ExpenseUpdate expense);

    void deleteById(String id);

}