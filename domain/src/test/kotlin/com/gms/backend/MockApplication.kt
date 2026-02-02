package com.gms.backend

import io.minio.MinioClient
import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.format.support.FormattingConversionService

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

    @Bean
    fun minioClient(
        @Value("\${minio.url}") url: String,
        @Value("\${minio.access-key}") accessKey: String,
        @Value("\${minio.secret-key}") secretKey: String
    ): MinioClient {
        return MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build()
    }

    @Bean
    fun mvcConversionService(): FormattingConversionService {
        return DefaultFormattingConversionService()
    }
}
