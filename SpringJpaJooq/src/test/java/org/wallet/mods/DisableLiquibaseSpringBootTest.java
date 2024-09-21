package org.wallet.mods;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "spring.liquibase.enabled=false",
                "spring.jpa.hibernate.ddl-auto=none"
        }
)
public interface DisableLiquibaseSpringBootTest {
}
