package com.gms.backend

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.gms.backend"])
@EnableJpaRepositories(basePackages = ["com.gms.backend.domain.domain.repository"])
@EntityScan(basePackages = ["com.gms.backend.domain.domain.model"])
class MockApplication {
    @Bean
    fun natsConnection(): Connection {
        // We read the dynamic URL set in BaseTest via System properties
        val natsUrl = System.getProperty("nats.url") ?: "nats://localhost:4222"
        return Nats.connect(natsUrl)
    }
}
