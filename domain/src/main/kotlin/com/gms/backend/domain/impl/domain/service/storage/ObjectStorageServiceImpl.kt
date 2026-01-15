package com.gms.backend.domain.impl.domain.service.storage

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.service.storage.ObjectStorageService
import io.minio.*
import io.minio.http.Method
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import java.util.concurrent.TimeUnit

@Service
@PreAuthorize("permitAll()")
class ObjectStorageServiceImpl (
    private val minioClient: MinioClient,
    private val objectStorageRepository: ObjectStorageRepository
) : ObjectStorageService {
    @Transactional
    @PreAuthorize("permitAll()")
    override fun uploadFile(
        file: MultipartFile,
        bucket: String,
        folder: String,
        actor: Actor
    ): ObjectStorage {
        val fileExtension = file.originalFilename?.substringAfterLast('.', "bin")
        val fileKey = "$folder/${UUID.randomUUID()}.$fileExtension"

        try {
            file.inputStream.use { inputStream ->
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucket)
                        .`object`(fileKey)
                        .stream(inputStream, file.size, -1)
                        .contentType(file.contentType ?: "application/octet-stream")
                        .build()
                )
            }

            val storageRecord = ObjectStorage().apply {
                this.bucket = bucket
                this.fileKey = fileKey
                this.name = file.originalFilename ?: "unnamed"
                this.fileSize = file.size.toString()
                this.mimeType = file.contentType ?: "application/octet-stream"
                this.tags = folder // This sets the tag to 'expense', 'profile', etc.
                this.createdBy = actor
                this.updatedBy = actor
                this.status = 1
            }
            return objectStorageRepository.save(storageRecord)
        } catch (e: Exception) {
            throw RuntimeException("Storage error: ${e.message}")
        }
    }
    @PreAuthorize("permitAll()")
    override fun getDownloadUrl(id: UUID): String {
        val metadata = objectStorageRepository.findById(id)
            .orElseThrow { RuntimeException("File record not found in database for ID: $id") }

        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(metadata.bucket)
                .`object`(metadata.fileKey)
                .expiry(7, TimeUnit.DAYS) // can set this from 1 minute to 7 days
                .build()
        )
    }
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
    }
}