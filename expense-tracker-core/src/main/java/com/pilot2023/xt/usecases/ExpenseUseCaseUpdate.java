package com.pilot2023.xt.usecases;

import com.pilot2023.xt.dataprovider.ExpenseDataProviderPersistence;
import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.dto.ExpenseUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseUseCaseUpdate {

    private final ExpenseDataProviderPersistence dataProvider;

    public Output execute(Input input) {
        Expense expense = dataProvider.update(input.getId(), input.getExpense());
        return Output.builder()
                .expense(expense)
                .build();
    }

    @Getter
    @Builder
    public static class Input {
        private String id;
        private ExpenseUpdate expense;
    }

    @Getter
    @Builder
    public static class Output {
        private Expense expense;
    }

}