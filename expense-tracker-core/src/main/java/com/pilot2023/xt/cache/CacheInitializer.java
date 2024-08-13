package com.pilot2023.xt.cache;

import com.pilot2023.xt.dataprovider.ExpenseCategoryDataProviderPersistence;
import com.pilot2023.xt.domain.ExpenseCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final ExpenseCategoryDataProviderPersistence dataProvider;
    @Value("${db.populateOnStartUp:false}")
    private boolean populateOnStartUp;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (Boolean.TRUE.equals(populateOnStartUp)) {
            populateDb();
        }
        List<ExpenseCategory> categories = dataProvider.getAll();
        ExpenseCategoryCache.setCategories(
                categories.stream()
                        .collect(Collectors.toMap(ExpenseCategory::getName, Function.identity()))
        );
        log.info(
                "loaded {} categories: {}",
                ExpenseCategoryCache.getCategories().size(),
                ExpenseCategoryCache.getCategories().keySet()
        );
    }

    private void populateDb() {
        dataProvider.deleteAll();
        dataProvider.saveAll(
                List.of(
                        ExpenseCategory.builder().name("Housing").build(),
                        ExpenseCategory.builder().name("Grocery").build(),
                        ExpenseCategory.builder().name("Transportation").build(),
                        ExpenseCategory.builder().name("Savings or Investments").build(),
                        ExpenseCategory.builder().name("Gifts and Donations").build(),
                        ExpenseCategory.builder().name("Loans").build(),
                        ExpenseCategory.builder().name("Insurance").build(),
                        ExpenseCategory.builder().name("Taxes").build(),
                        ExpenseCategory.builder().name("Legal").build(),
                        ExpenseCategory.builder().name("Personal Care").build(),
                        ExpenseCategory.builder().name("Pets").build(),
                        ExpenseCategory.builder().name("Gym").build(),
                        ExpenseCategory.builder().name("Dining out").build(),
                        ExpenseCategory.builder().name("Nightlife").build(),
                        ExpenseCategory.builder().name("Entertainment").build()
                )
        );
    }

}