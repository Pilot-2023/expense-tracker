package com.pilot2023.xt.api;

import com.pilot2023.xt.domain.Direction;
import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.domain.OrderBy;
import com.pilot2023.xt.dto.ExpenseCreate;
import com.pilot2023.xt.dto.ExpenseUpdate;
import com.pilot2023.xt.util.ExpenseTrackerConstants;
import com.pilot2023.xt.validation.ValidExpenseCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/expense")
public interface ExpenseApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Expense> createExpense(@Valid @RequestBody ExpenseCreate expense);

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Expense> getExpense(
            @PathVariable @Pattern(regexp = ExpenseTrackerConstants.ID_REGEX, message = ExpenseTrackerConstants.ID_INVALID_MSG) String id
    );

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<List<Expense>> listExpenses(
            @RequestParam(required = false, defaultValue = ExpenseTrackerConstants.DEFAULT_OFFSET)
                @Min(value = 0, message = ExpenseTrackerConstants.OFFSET_INVALID_MSG) Integer offset,
            @RequestParam(required = false, defaultValue = ExpenseTrackerConstants.DEFAULT_LIMIT)
                @Min(value = 1, message = ExpenseTrackerConstants.LIMIT_INVALID_MSG)
                @Max(value = ExpenseTrackerConstants.MAX_LIMIT, message = ExpenseTrackerConstants.LIMIT_INVALID_MSG) Integer limit,
            @RequestParam(required = false)
                @Pattern(regexp = ExpenseTrackerConstants.FIELDS_REGEX, message = ExpenseTrackerConstants.FIELDS_INVALID_MSG) String fields,
            @RequestParam(required = false)
                @Pattern(regexp = ExpenseTrackerConstants.MULTIPLE_IDS_REGEX, message = ExpenseTrackerConstants.MULTIPLE_IDS_REGEX) String ids,
            @RequestParam(required = false)
                @ValidExpenseCategory String category,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false)
                @Min(value = 0, message = ExpenseTrackerConstants.EXPENSE_COST_INVALID_MSG) Float cost,
            @RequestParam(required = false)
                @Min(value = 0, message = ExpenseTrackerConstants.EXPENSE_COST_GTE_INVALID_MSG) Float costGte,
            @RequestParam(required = false)
                @Min(value = 0, message = ExpenseTrackerConstants.EXPENSE_COST_LTE_INVALID_MSG) Float costLte,
            @RequestParam(required = false, defaultValue = ExpenseTrackerConstants.DEFAULT_ORDER) List<OrderBy> orderBy,
            @RequestParam(required = false, defaultValue = ExpenseTrackerConstants.DEFAULT_DIRECTION) List<Direction> direction
    );

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Expense> updateExpense(
            @PathVariable @Pattern(regexp = ExpenseTrackerConstants.ID_REGEX, message = ExpenseTrackerConstants.ID_INVALID_MSG) String id,
            @Valid @RequestBody ExpenseUpdate expense
    );

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteExpense(
            @PathVariable @Pattern(regexp = ExpenseTrackerConstants.ID_REGEX, message = ExpenseTrackerConstants.ID_INVALID_MSG) String id
    );

}