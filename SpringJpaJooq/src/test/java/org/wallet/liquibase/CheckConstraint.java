package org.wallet.liquibase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckConstraint {

    private String name;
    private String checkClause;
    private String database;
    private String schema;

}
