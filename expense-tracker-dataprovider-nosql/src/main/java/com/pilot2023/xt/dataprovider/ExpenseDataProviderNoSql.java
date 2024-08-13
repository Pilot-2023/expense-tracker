package com.pilot2023.xt.dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.pilot2023.xt.document.ExpenseDocument;
import com.pilot2023.xt.domain.Direction;
import com.pilot2023.xt.domain.Expense;
import com.pilot2023.xt.domain.OrderBy;
import com.pilot2023.xt.dto.ExpenseCreate;
import com.pilot2023.xt.dto.ExpenseUpdate;
import com.pilot2023.xt.exception.BusinessException;
import com.pilot2023.xt.exception.ExceptionCode;
import com.pilot2023.xt.mapper.ExpenseMapperDataProviderNoSql;
import com.pilot2023.xt.usecases.ExpenseUseCaseList;
import com.pilot2023.xt.util.ExpenseTrackerConstants;
import com.pilot2023.xt.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service("ExpenseDataProviderNoSql")
@RequiredArgsConstructor
public class ExpenseDataProviderNoSql implements ExpenseDataProviderPersistence {

    private final MongoTemplate mongodb;
    private final ExpenseMapperDataProviderNoSql mapper;

    private static Comparator<ExpenseDocument> getComparator(OrderBy order, Direction direction) {
        Comparator<ExpenseDocument> comparator;
        switch (order) {
            case DATE -> comparator = Comparator.comparing(e -> e.getCreatedAt().toLocalDate());
            case COST -> comparator = Comparator.comparing(ExpenseDocument::getCost);
            default ->
                    throw new BusinessException(ExceptionCode.PARAMETER_VALIDATION_ERROR, String.format("invalid 'orderBy' %s", order));
        }
        return Direction.ASC.equals(direction) ? comparator : comparator.reversed();
    }

    private static void sort(
            List<ExpenseDocument> expenses,
            List<OrderBy> orderByList,
            List<Direction> directionList
    ) {
        if (!CollectionUtils.isEmpty(expenses) && !CollectionUtils.isEmpty(orderByList) && !CollectionUtils.isEmpty(directionList)) {
            Comparator<ExpenseDocument> comparator = getComparator(orderByList.get(0), directionList.get(0));
            for (int i = 1; i < orderByList.size(); i++) {
                comparator = comparator.thenComparing(
                        getComparator(orderByList.get(i), directionList.get(i))
                );
            }
            expenses = new ArrayList<>(expenses); // convert immutable to mutable list for sort() to work
            expenses.sort(comparator);
        }
    }

    @Override
    public Expense create(ExpenseCreate expense) {
        ExpenseDocument document = mapper.toExpenseDocument(expense);
        document = mongodb.save(document);
        return mapper.toExpense(document);
    }

    @Override
    public Expense getById(String id) {
        ExpenseDocument document = getDocumentById(id);
        return mapper.toExpense(document);
    }

    @Override
    public List<Expense> list(ExpenseUseCaseList.Input input) {
        List<Criteria> criteriaList = new ArrayList<>();
        if (Util.inputFieldFilled(input.getIds())) {
            String[] ids = input.getIds().split(",");
            if (ids.length != 0) {
                criteriaList.add(Criteria.where("id").in(Arrays.asList(ids)));
            }
        }
        if (Util.inputFieldFilled(input.getCategory())) {
            criteriaList.add(Criteria.where("category").is(input.getCategory()));
        }
        if (Util.inputFieldGiven(input.getCost())) {
            criteriaList.add(Criteria.where("cost").is(input.getCost()));
        }
        if (Util.inputFieldGiven(input.getCostGte())) {
            criteriaList.add(Criteria.where("cost").gte(input.getCostGte()));
        }
        if (Util.inputFieldGiven(input.getCostLte())) {
            criteriaList.add(Criteria.where("cost").lte(input.getCostLte()));
        }
        if (Util.inputFieldGiven(input.getDate())) {
            // DB 'date'    = 2023-10-05T14:45:27.352+00:00
            // input 'date' = 2023-10-05
            // input 'date' range = [2023-10-05T00:00:00.000+00:00, 2023-10-05T23:59:59.999999999+00:00]
            LocalDateTime from = LocalDateTime.of(input.getDate(), LocalTime.MIN);
            LocalDateTime to = LocalDateTime.of(input.getDate(), LocalTime.MAX);
            criteriaList.add(Criteria.where(ExpenseTrackerConstants.CREATED_AT_DB_FIELD).gte(from).lte(to));
        }
        if (Util.inputFieldGiven(input.getFrom())) {
            // input 'from'      = 2023-10-05
            // calculated 'from' = 2023-10-05T00:00:00.000+00:00
            LocalDateTime from = LocalDateTime.of(input.getFrom(), LocalTime.MIN);
            criteriaList.add(Criteria.where(ExpenseTrackerConstants.CREATED_AT_DB_FIELD).gte(from));
        }
        if (Util.inputFieldGiven(input.getTo())) {
            // input 'to'      = 2023-10-05
            // calculated 'to' = 2023-10-05T23:59:59.999999999+00:00
            LocalDateTime to = LocalDateTime.of(input.getTo(), LocalTime.MAX);
            criteriaList.add(Criteria.where(ExpenseTrackerConstants.CREATED_AT_DB_FIELD).lte(to));
        }
        List<ExpenseDocument> documents = getPage(
                input.getOffset(),
                input.getLimit(),
                input.getFields(),
                criteriaList,
                input.getOrderBy(),
                input.getDirection()
        );
        return mapper.toExpenseList(documents);
    }

    @Override
    public Expense update(String id, ExpenseUpdate expense) {
        ExpenseDocument document = getDocumentById(id);
        mapper.updateExpenseDocument(document, expense);
        document = mongodb.save(document);
        return mapper.toExpense(document);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        DeleteResult result = mongodb.remove(query, ExpenseDocument.class);
        if (result.getDeletedCount() == 0) {
            throw new BusinessException(ExceptionCode.NOT_FOUND, String.format("expense %s not found", id));
        }
    }

    private ExpenseDocument getDocumentById(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        ExpenseDocument document = mongodb.findOne(query, ExpenseDocument.class);
        document = Optional.ofNullable(document).orElseThrow(() ->
                new BusinessException(ExceptionCode.NOT_FOUND, String.format("expense %s not found", id))
        );
        return document;
    }

    private List<ExpenseDocument> getPage(
            int offset,
            int limit,
            String fields,
            List<Criteria> criteriaList,
            List<OrderBy> orderByList,
            List<Direction> directionList
    ) {
        List<AggregationOperation> operations = new ArrayList<>(
                criteriaList.stream()
                        .map(Aggregation::match)
                        .map(AggregationOperation.class::cast)
                        .toList()
        );
        operations.add(Aggregation.skip(offset));
        operations.add(Aggregation.limit(limit));
        if (Util.inputFieldFilled(fields)) {
            fields = fields.replaceAll(",?date", "");

            // 'createdAt' and 'cost' is always required for sort() to work
            fields = fields.replaceAll(",?" + ExpenseTrackerConstants.CREATED_AT_DB_FIELD, "");
            fields = fields.replaceAll(",?cost", "");
            if (StringUtils.hasText(fields)) {
                fields = fields + ",";
            }
            fields = fields + ExpenseTrackerConstants.CREATED_AT_DB_FIELD + ",cost";

            String[] projection = fields.split(",");
            operations.add(Aggregation.project(projection));
        }
        Aggregation aggregation = Aggregation.newAggregation(operations);
        List<ExpenseDocument> expenses = mongodb.aggregate(aggregation, ExpenseDocument.class, ExpenseDocument.class).getMappedResults();
        sort(expenses, orderByList, directionList);
        return expenses;
    }

}