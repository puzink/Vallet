package org.wallet.liquibase;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "spring.liquibase.enabled=false",
                "spring.jpa.hibernate.ddl-auto=none"
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class DisableLiquibaseSpringBootTest {
}
