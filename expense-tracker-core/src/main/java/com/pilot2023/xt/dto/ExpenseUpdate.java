package com.pilot2023.xt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pilot2023.xt.util.ExpenseTrackerConstants;
import com.pilot2023.xt.validation.ValidExpenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseUpdate {

    @ValidExpenseCategory
    private String category;

    @Pattern(regexp = ExpenseTrackerConstants.EXPENSE_DESCRIPTION_REGEX, message = ExpenseTrackerConstants.EXPENSE_DESCRIPTION_INVALID_MSG)
    private String description;

    @Min(value = 0, message = ExpenseTrackerConstants.EXPENSE_COST_INVALID_MSG)
    private Float cost;

}