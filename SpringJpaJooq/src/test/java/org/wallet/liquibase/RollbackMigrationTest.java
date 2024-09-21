package org.wallet.liquibase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.wallet.mods.ContainerProvider;
import org.wallet.mods.DisableLiquibaseSpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ImportTestcontainers(ContainerProvider.class)
public class RollbackMigrationTest implements DisableLiquibaseSpringBootTest {

    @Autowired
    private LiquibaseService liquibaseService;

    @Autowired
    private DatabaseMetaDao databaseMetaDao;

    @Test
    public void testRollback() {
        assertThatCode(() -> liquibaseService.updateCount(1)).doesNotThrowAnyException();
        assertThatCode(() -> liquibaseService.rollbackCount(1)).doesNotThrowAnyException();
        assertThat(databaseMetaDao.findTableBySchemaAndName("public", "wallet"))
                .isEmpty();
    }
}
