package fi.metropolia.omagr.grimaux;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Bootstraps the application against an isolated in-memory database.
 * Production Flyway migrations target the existing MariaDB schema and
 * are intentionally not run in this smoke test.
 */
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:grimaux_context_test;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class GrimauxApplicationTests {

    @Test
    void contextLoads() {
    }
}
