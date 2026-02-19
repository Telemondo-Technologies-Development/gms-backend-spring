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

@SpringBootTest(classes = [MockApplication::class])
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@Testcontainers
@ActiveProfiles("test")
@WithUserDetails("admin")
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
        // Define containers without @Container annotation
        val mysql = MySQLContainer("mysql:9.2")
            .withDatabaseName("testdb")
            .withEnv("MYSQL_ROOT_PASSWORD", "root")
            .withEnv("MYSQL_LOG_BIN_TRUST_FUNCTION_CREATORS", "1")

        val minio = MinIOContainer("minio/minio:RELEASE.2025-09-07T16-13-09Z")

        val nats = GenericContainer("nats:latest")
            .withExposedPorts(4222)
            .waitingFor(Wait.forListeningPort())

        init {
            // Start containers manually
            mysql.start()
            minio.start()
            nats.start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            val jdbcUrl = "${mysql.jdbcUrl}?useSSL=false&allowPublicKeyRetrieval=true"
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

            // Nats
            registry.add("nats.url") { "nats://${nats.host}:${nats.getMappedPort(4222)}" }
        }
    }
}