package com.pilot2023.xt.usecases;

import com.pilot2023.xt.dataprovider.ExpenseDataProviderPersistence;
import com.pilot2023.xt.domain.Direction;
import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.domain.OrderBy;
import com.pilot2023.xt.exception.BusinessException;
import com.pilot2023.xt.exception.ExceptionCode;
import com.pilot2023.xt.util.Util;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpenseUseCaseList {

    private final ExpenseDataProviderPersistence dataProvider;

    private static void treatInput(Input input) {
        if (Util.inputFieldGiven(input.getCost())) {
            // if 'cost' is given, do not query by cost range
            input.costGte = null;
            input.costLte = null;
        }
        if (Util.inputFieldGiven(input.getDate())) {
            // if 'date' is given, do not query by date range
            input.from = null;
            input.to = null;
        }
    }

    private static void validateInput(Input input) {
        if (input.getOrderBy().size() != input.getDirection().size()) {
            throw new BusinessException(
                    ExceptionCode.PARAMETER_VALIDATION_ERROR,
                    String.format(
                            "invalid 'orderBy' and 'direction' tuple. Given %s 'orderBy' and %s 'direction'. Both sizes must match.",
                            input.getOrderBy().size(),
                            input.getDirection().size()
                    )
            );
        }
        if (Util.inputFieldGiven(input.getCostGte()) && Util.inputFieldGiven(input.getCostLte()) && input.getCostLte() < input.getCostGte()) {
            throw new BusinessException(ExceptionCode.PARAMETER_VALIDATION_ERROR, "invalid cost range: 'costLte' must be greater than or equal to 'costGte'");
        }
        if (Util.inputFieldGiven(input.getFrom()) && Util.inputFieldGiven(input.getTo()) && input.getTo().isBefore(input.getFrom())) {
            throw new BusinessException(ExceptionCode.PARAMETER_VALIDATION_ERROR, "invalid date range: 'to' must be after 'from'");
        }
    }

    public Output execute(Input input) {
        validateInput(input);
        treatInput(input);
        log.info("listing expenses by (treated) criteria {}", input);
        List<Expense> expenses = dataProvider.list(input);
        return Output.builder()
                .expenses(expenses)
                .build();
    }

    @Getter
    @Builder
    @ToString
    public static class Input {
        private Integer offset;
        private Integer limit;
        private String fields;
        private String ids;
        private String category;
        private LocalDate date;
        private LocalDate from;
        private LocalDate to;
        private Float cost;
        private Float costGte;
        private Float costLte;
        private List<OrderBy> orderBy;
        private List<Direction> direction;
    }

    @Getter
    @Builder
    public static class Output {
        private List<Expense> expenses;
    }

}