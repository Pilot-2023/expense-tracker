package com.pilot2023.xt.validation;

import com.pilot2023.xt.util.ExpenseTrackerConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ExpenseCategoryValidator.class)
public @interface ValidExpenseCategory {

    String message() default ExpenseTrackerConstants.EXPENSE_CATEGORY_NOT_FOUND_MSG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
