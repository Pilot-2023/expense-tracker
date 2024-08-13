package com.pilot2023.xt.usecases;

import com.pilot2023.xt.dataprovider.ExpenseDataProviderPersistence;
import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.dto.ExpenseCreate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseUseCaseCreate {

    private final ExpenseDataProviderPersistence dataProvider;

    public Output execute(Input input) {
        Expense expense = dataProvider.create(input.getExpense());
        return Output.builder()
                .expense(expense)
                .build();
    }

    @Getter
    @Builder
    public static class Input {
        private ExpenseCreate expense;
    }

    @Getter
    @Builder
    public static class Output {
        private Expense expense;
    }

}