package com.pilot2023.xt.validation;

import com.pilot2023.xt.cache.ExpenseCategoryCache;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class ExpenseCategoryValidator implements ConstraintValidator<ValidExpenseCategory, String> {

    @Override
    public boolean isValid(String category, ConstraintValidatorContext context) {
        if (Objects.isNull(category)) {
            // leave null-checking to @NotNull
            return true;
        }
        formatMessage(context, category);
        return ExpenseCategoryCache.getCategories().containsKey(category);
    }

    private void formatMessage(ConstraintValidatorContext context, String category) {
        String msg = context.getDefaultConstraintMessageTemplate();
        String formattedMsg = String.format(msg, category);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(formattedMsg)
                .addConstraintViolation();
    }
}
