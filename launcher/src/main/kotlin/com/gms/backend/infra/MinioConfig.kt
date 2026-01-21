package com.gms.backend.infra

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {

    @Value("\${minio.url}")
    private lateinit var url: String

    @Value("\${minio.accessKey}")
    private lateinit var accessKey: String

    @Value("\${minio.secretKey}")
    private lateinit var secretKey: String

    @Value("\${minio.bucket.public}")
    private lateinit var publicBucket: String

    @Value("\${minio.bucket.private}")
    private lateinit var privateBucket: String

    @Bean
    fun minioClient(): MinioClient {
        // Create the client
        val client = MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build()

        // Ensure buckets exist on startup
        ensureBucketExists(client, publicBucket)
        ensureBucketExists(client, privateBucket)

        return client
    }
}
    private fun ensureBucketExists(client: MinioClient, bucketName: String) {
        val exists = client.bucketExists(
            BucketExistsArgs.builder().bucket(bucketName).build()
        )
        if (!exists) {
            client.makeBucket(
                MakeBucketArgs.builder().bucket(bucketName).build()
            )
            println("MinIO: Created missing bucket: $bucketName")
        }
    }
