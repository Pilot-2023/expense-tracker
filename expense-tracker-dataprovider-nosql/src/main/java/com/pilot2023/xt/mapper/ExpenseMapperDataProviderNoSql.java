package com.pilot2023.xt.mapper;

import com.pilot2023.xt.document.ExpenseCategoryDocument;
import com.pilot2023.xt.document.ExpenseDocument;
import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.domain.ExpenseCategory;
import com.pilot2023.xt.dto.ExpenseCreate;
import com.pilot2023.xt.dto.ExpenseUpdate;
import com.pilot2023.xt.util.ExpenseTrackerConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {UUID.class}
)
public interface ExpenseMapperDataProviderNoSql {

    @Mapping(target = "date", source = ExpenseTrackerConstants.CREATED_AT_DB_FIELD)
    Expense toExpense(ExpenseDocument document);

    List<Expense> toExpenseList(List<ExpenseDocument> documents);

    @Mapping(target = "dbId", ignore = true)
    @Mapping(target = "id", ignore = true) // do no let update the ID
    void updateExpenseDocument(@MappingTarget ExpenseDocument document, ExpenseUpdate expense);

    @Mapping(target = "dbId", ignore = true)
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())") // auto-generate random ID
    ExpenseDocument toExpenseDocument(ExpenseCreate expense);

    ExpenseCategory toExpenseCategory(ExpenseCategoryDocument documents);

    List<ExpenseCategory> toExpenseCategoryList(List<ExpenseCategoryDocument> documents);

    @Mapping(target = "dbId", ignore = true)
    ExpenseCategoryDocument toExpenseCategoryDocument(ExpenseCategory category);

    List<ExpenseCategoryDocument> toExpenseCategoryDocumentList(List<ExpenseCategory> categories);

}