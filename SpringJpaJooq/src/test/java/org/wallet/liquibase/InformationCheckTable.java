package org.wallet.liquibase;

public class InformationCheckTable {

    public static final String SCHEMA = "information_schema";
    public static final String TABLE_NAME = "check_constraints";

    public static final String CONSTRAINT_NAME = "constraint_name";
    public static final String CONSTRAINT_CATALOG = "constraint_catalog";
    public static final String CONSTRAINT_SCHEMA = "constraint_schema";
    public static final String CHECK_CLAUSE = "check_clause";

    public static String getFullName() {
        return SCHEMA + "." + TABLE_NAME;
    }
}
