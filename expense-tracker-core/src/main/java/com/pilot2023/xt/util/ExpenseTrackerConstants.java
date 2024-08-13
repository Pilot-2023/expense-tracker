package com.pilot2023.xt.util;

public class ExpenseTrackerConstants {

    // default values
    public static final String DEFAULT_OFFSET = "0";
    public static final String DEFAULT_LIMIT = "10";
    public static final int MAX_LIMIT = 100;
    public static final String DEFAULT_ORDER = "DATE";
    public static final String DEFAULT_DIRECTION = "DESC";

    // database
    public static final String CREATED_AT_DB_FIELD = "createdAt";

    // required fields validation
    public static final String EXPENSE_CATEGORY_MANDATORY_MSG = "'category' is mandatory";
    public static final String EXPENSE_COST_MANDATORY_MSG = "'cost' is mandatory";

    // fields values validation
    public static final String EXPENSE_CATEGORY_NOT_FOUND_MSG = "expense 'category' %s not found";
    public static final String EXPENSE_COST_INVALID_MSG = "'cost' must be positive";
    public static final String EXPENSE_COST_GTE_INVALID_MSG = "'costGte' must be positive";
    public static final String EXPENSE_COST_LTE_INVALID_MSG = "'costLte' must be positive";
    public static final String OFFSET_INVALID_MSG = "'offset' must be positive";
    public static final String LIMIT_INVALID_MSG = "'limit' must be in the range [1, " + MAX_LIMIT + "]";
    public static final String EXPENSE_DESCRIPTION_REGEX = "[ \\wÀ-ú\\.:,;\\-\\[\\]()]{1,100}";
    public static final String EXPENSE_DESCRIPTION_INVALID_MSG = "'description' must match: " + EXPENSE_DESCRIPTION_REGEX;
    public static final String FIELDS_REGEX = "[\\w,]{1,100}";
    public static final String FIELDS_INVALID_MSG = "'fields' must match: " + FIELDS_REGEX;
    public static final String ID_REGEX = "[a-fA-F\\d\\-]{36}";
    public static final String MULTIPLE_IDS_REGEX = "[a-fA-F\\d\\-\\,]{36," + (36 * (MAX_LIMIT + 1)) + "}";
    public static final String ID_INVALID_MSG = "'id' must match: " + ID_REGEX;

    private ExpenseTrackerConstants() {
        throw new IllegalStateException("Cannot instantiate a Utils class");
    }

}