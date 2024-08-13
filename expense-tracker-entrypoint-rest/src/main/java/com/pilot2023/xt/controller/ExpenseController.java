package com.pilot2023.xt.controller;

import com.pilot2023.xt.api.ExpenseApi;
import com.pilot2023.xt.domain.Direction;
import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.domain.OrderBy;
import com.pilot2023.xt.dto.ExpenseCreate;
import com.pilot2023.xt.dto.ExpenseUpdate;
import com.pilot2023.xt.usecases.ExpenseUseCaseCreate;
import com.pilot2023.xt.usecases.ExpenseUseCaseDelete;
import com.pilot2023.xt.usecases.ExpenseUseCaseGet;
import com.pilot2023.xt.usecases.ExpenseUseCaseList;
import com.pilot2023.xt.usecases.ExpenseUseCaseUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class ExpenseController implements ExpenseApi {

    private final ExpenseUseCaseCreate createUseCase;
    private final ExpenseUseCaseGet getUseCase;
    private final ExpenseUseCaseList listUseCase;
    private final ExpenseUseCaseUpdate updateUseCase;
    private final ExpenseUseCaseDelete deleteUseCase;

    public ResponseEntity<Expense> createExpense(ExpenseCreate expense) {
        log.info("creating expense {}", expense);
        ExpenseUseCaseCreate.Input input = ExpenseUseCaseCreate.Input.builder()
                .expense(expense)
                .build();
        ExpenseUseCaseCreate.Output output = createUseCase.execute(input);
        return new ResponseEntity<>(output.getExpense(), HttpStatus.CREATED);
    }

    public ResponseEntity<Expense> getExpense(String id) {
        log.info("getting expense {}", id);
        ExpenseUseCaseGet.Input input = ExpenseUseCaseGet.Input.builder()
                .id(id)
                .build();
        ExpenseUseCaseGet.Output output = getUseCase.execute(input);
        return new ResponseEntity<>(output.getExpense(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Expense>> listExpenses(
            Integer offset,
            Integer limit,
            String fields,
            String ids,
            String category,
            LocalDate date,
            LocalDate from,
            LocalDate to,
            Float cost,
            Float costGte,
            Float costLte,
            List<OrderBy> orderBy,
            List<Direction> direction
    ) {
        ExpenseUseCaseList.Input input = ExpenseUseCaseList.Input.builder()
                .offset(offset)
                .limit(limit)
                .fields(fields)
                .ids(ids)
                .category(category)
                .date(date)
                .from(from)
                .to(to)
                .cost(cost)
                .costGte(costGte)
                .costLte(costLte)
                .orderBy(orderBy)
                .direction(direction)
                .build();
        log.info("listing expenses by criteria {}", input);
        ExpenseUseCaseList.Output output = listUseCase.execute(input);
        return new ResponseEntity<>(output.getExpenses(), HttpStatus.OK);
    }

    public ResponseEntity<Expense> updateExpense(String id, ExpenseUpdate expense) {
        log.info("updating expense {} with new data {}", id, expense);
        ExpenseUseCaseUpdate.Input input = ExpenseUseCaseUpdate.Input.builder()
                .id(id)
                .expense(expense)
                .build();
        ExpenseUseCaseUpdate.Output output = updateUseCase.execute(input);
        return new ResponseEntity<>(output.getExpense(), HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteExpense(String id) {
        log.info("deleting expense {}", id);
        ExpenseUseCaseDelete.Input input = ExpenseUseCaseDelete.Input.builder()
                .id(id)
                .build();
        ExpenseUseCaseDelete.Output output = deleteUseCase.execute(input);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}