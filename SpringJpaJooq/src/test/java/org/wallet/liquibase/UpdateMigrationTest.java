package org.wallet.liquibase;


import liquibase.exception.CommandExecutionException;
import lombok.val;
import org.jooq.Field;
import org.jooq.Table;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.wallet.mods.ContainerProvider;
import org.wallet.mods.DisableLiquibaseSpringBootTest;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

// для использования IoC-контейнера в @BeforeAll/@AfterAll
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ImportTestcontainers(ContainerProvider.class)
public class UpdateMigrationTest implements DisableLiquibaseSpringBootTest {

    @Autowired
    private LiquibaseService liquibaseService;

    @Autowired
    private DatabaseMetaDaoImpl databaseMetaDao;

    private Table<?> table;

    @BeforeAll
    public void setup() throws CommandExecutionException {
        System.out.println("Test setup started...");
        liquibaseService.updateCount(1);
        System.out.println("Test setup is done.");
    }

    @AfterAll
    public void teardown() throws CommandExecutionException {
        System.out.println("Test teardown started...");
        liquibaseService.rollbackCount(1);
        System.out.println("Test teardown is done.");

    }

    @Test
    @Order(Integer.MIN_VALUE)
    public void tableExists() {
        String schemaName = "public";
        String tableName = "wallet";
        val table0 = databaseMetaDao.findTableBySchemaAndName(schemaName, tableName);
        assertThat(table0).isPresent();
        table = table0.get();
    }

    @Test
    public void checkColumnList() {
        val columns = Arrays.stream(table.fields())
                .map(Field::getName)
                .toList();
        val expected = new String[]{"uuid", "amount"};

        assertThat(columns).containsExactlyInAnyOrder(expected);
    }

    @Test
    public void checkColumnTypes() {
        val unwrappedTable = table;
        val uuid = unwrappedTable.field("uuid");
        val amount = unwrappedTable.field("amount");

        assertThat(uuid).isNotNull();
        assertThat(uuid.getType()).isEqualTo(UUID.class);

        assertThat(amount).isNotNull();
        assertThat(amount.getType()).isEqualTo(Long.class);
    }

    @Test
    public void checkConstraints() {
        val unwrappedTable = table;
        val checkConstraintName = "wallet_amount_check";

        val primaryKey = unwrappedTable.getPrimaryKey();
        assertThat(primaryKey).isNotNull();
        assertThat(primaryKey.getFields()).hasSize(1);
        assertThat(primaryKey.getFields().getFirst().getName()).isEqualTo("uuid");

        val walletAmountCheck = databaseMetaDao.findConstraintByName(checkConstraintName);
        assertThat(walletAmountCheck).isPresent();
        assertThat(walletAmountCheck.get().getCheckClause()).isEqualTo("((amount >= 0))");

    }


}
