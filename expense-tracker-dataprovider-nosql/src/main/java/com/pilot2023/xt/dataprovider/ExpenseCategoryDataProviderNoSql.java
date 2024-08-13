package com.pilot2023.xt.dataprovider;

import com.pilot2023.xt.document.ExpenseCategoryDocument;
import com.pilot2023.xt.domain.ExpenseCategory;
import com.pilot2023.xt.exception.BusinessException;
import com.pilot2023.xt.exception.ExceptionCode;
import com.pilot2023.xt.mapper.ExpenseMapperDataProviderNoSql;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("ExpenseCategoryDataProviderNoSql")
@RequiredArgsConstructor
public class ExpenseCategoryDataProviderNoSql implements ExpenseCategoryDataProviderPersistence {

    private final MongoTemplate mongo;
    private final ExpenseMapperDataProviderNoSql mapper;

    @Override
    public List<ExpenseCategory> getAll() {
        List<ExpenseCategoryDocument> documents = mongo.findAll(ExpenseCategoryDocument.class);
        if (CollectionUtils.isEmpty(documents)) {
            throw new BusinessException(ExceptionCode.NOT_FOUND, "no expense categories found");
        }
        return mapper.toExpenseCategoryList(documents);
    }

    @Override
    public void saveAll(List<ExpenseCategory> categories) {
        BulkOperations bulk = mongo.bulkOps(BulkOperations.BulkMode.UNORDERED, ExpenseCategoryDocument.class);
        bulk = bulk.insert(mapper.toExpenseCategoryDocumentList(categories));
        bulk.execute();
    }

    @Override
    public void deleteAll() {
        mongo.remove(new Query(), ExpenseCategoryDocument.class);
    }

}