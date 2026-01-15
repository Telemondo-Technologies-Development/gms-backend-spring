package com.gms.backend.impl.service

import com.github.database.rider.core.api.configuration.DBUnit
import com.github.database.rider.spring.api.DBRider
import com.gms.backend.MockApplication
import jakarta.transaction.Transactional
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(classes = [MockApplication::class])
@ActiveProfiles("test")
@WithUserDetails("admin")
@Testcontainers
@Transactional
@DBRider
@DBUnit(
    schema = "testdb",
    caseSensitiveTableNames = true,
//    alwaysCleanBefore = true,
//    alwaysCleanAfter = true,
)
class BaseTest {
    companion object {
        @Container
        @JvmStatic
        val mysql: MySQLContainer<*> =
            MySQLContainer("mysql:9.2")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")

        @Container
        @JvmStatic
        val nats: GenericContainer<*> = GenericContainer("nats:latest")
            .withExposedPorts(4222)
            .waitingFor(
                Wait.forListeningPort()
            )

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysql::getJdbcUrl)
            registry.add("spring.datasource.username", mysql::getUsername)
            registry.add("spring.datasource.password", mysql::getPassword)

            // Ensure Flyway points to the Testcontainers DB
            registry.add("spring.flyway.url", mysql::getJdbcUrl)
            registry.add("spring.flyway.user", mysql::getUsername)
            registry.add("spring.flyway.password", mysql::getPassword)
        }
    }
}