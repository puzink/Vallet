package org.wallet.liquibase;

import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.val;
import static org.wallet.liquibase.InformationCheckTable.*;

@Repository
@AllArgsConstructor
public class DatabaseMetaDaoImpl implements DatabaseMetaDao {

    private final DSLContext dsl;

    public Optional<CheckConstraint> findConstraintByName(@NotNull String constraintName) {
        val queryPart = dsl.select()
                .from(DSL.table(DSL.unquotedName(InformationCheckTable.getFullName())))
                .where(field(CONSTRAINT_NAME).eq(val(constraintName)))
                .and(field(CONSTRAINT_CATALOG).eq(val("test")))
                .and(field(CONSTRAINT_SCHEMA).eq(val("public")));

        CheckConstraint res = queryPart.fetchAny(r -> new CheckConstraint(
                r.get(CONSTRAINT_NAME, String.class),
                r.get(CHECK_CLAUSE, String.class),
                r.get(CONSTRAINT_CATALOG, String.class),
                r.get(CONSTRAINT_SCHEMA, String.class)
        ));
        return Optional.ofNullable(res);
    }

    @Override
    public Optional<Table<?>> findTableBySchemaAndName(@NotNull String schema, @NotNull String tableName) {
        return dsl.meta()
                .filterSchemas(s -> Objects.equals(s.getName(), schema))
                .getTables().stream()
                .filter(t -> Objects.equals(t.getName(), tableName))
                .findAny();
    }


}
