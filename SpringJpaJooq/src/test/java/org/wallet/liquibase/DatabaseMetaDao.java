package org.wallet.liquibase;

import org.jetbrains.annotations.NotNull;
import org.jooq.Table;

import java.util.Optional;

public interface DatabaseMetaDao {

    Optional<CheckConstraint> findConstraintByName(@NotNull String constraintName);

    Optional<Table<?>> findTableBySchemaAndName(@NotNull String schema, @NotNull String tableName);
}
