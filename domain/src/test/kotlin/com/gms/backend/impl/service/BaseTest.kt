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
import org.testcontainers.containers.MinIOContainer
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
    cacheConnection = false
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
                .withEnv("MYSQL_ROOT_PASSWORD", "test")
                .withEnv("MYSQL_LOG_BIN_TRUST_FUNCTION_CREATORS", "1")

        @Container
        @JvmStatic
        val minio: MinIOContainer = MinIOContainer("minio/minio:RELEASE.2025-09-07T16-13-09Z")

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
            val jdbcUrl = mysql.jdbcUrl
            val rootPassword = mysql.password

            registry.add("spring.datasource.url") { jdbcUrl }
            registry.add("spring.datasource.username") { "root" }
            registry.add("spring.datasource.password") { rootPassword }

            // Ensure Flyway also uses root to execute the migration scripts
            registry.add("spring.flyway.url") { jdbcUrl }
            registry.add("spring.flyway.user") { "root" }
            registry.add("spring.flyway.password") { rootPassword }

            // MiniO
            registry.add("minio.url", minio::getS3URL)
            registry.add("minio.access-key", minio::getUserName)
            registry.add("minio.secret-key", minio::getPassword)
        }
    }
}