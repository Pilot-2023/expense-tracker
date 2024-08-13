package com.pilot2023.xt.usecases;

import com.pilot2023.xt.dataprovider.ExpenseDataProviderPersistence;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseUseCaseDelete {

    private final ExpenseDataProviderPersistence dataProvider;

    public Output execute(Input input) {
        dataProvider.deleteById(input.getId());
        return Output.builder().build();
    }

    @Getter
    @Builder
    public static class Input {
        private String id;
    }

    @Getter
    @Builder
    public static class Output {

    }

}