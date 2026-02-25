package com.gms.backend.domain.impl.domain.service.storage

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.storage.ObjectStorageService
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.http.Method
import jakarta.transaction.Transactional
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*
import java.util.concurrent.TimeUnit

@Service
@PreAuthorize("denyAll()")
class ObjectStorageServiceImpl (
    private val minioClient: MinioClient,
    private val objectStorageRepository: ObjectStorageRepository,
    private val actorRepository: ActorRepository
) : ObjectStorageService {
    @Transactional
    @PreAuthorize("hasAuthority('objectStorage_create')")
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
    @PreAuthorize("hasAuthority('objectStorage_read')")
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
    @PreAuthorize("hasAuthority('objectStorage_delete')")
    override fun deleteFile(id: UUID) {
        val storage = objectStorageRepository.findById(id)
            .orElseThrow { RuntimeException("File not found") }

        // 1. Remove from MinIO
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(storage.bucket)
                .`object`(storage.fileKey)
                .build()
        )

        // 2. Remove from Database
        objectStorageRepository.delete(storage)
    }
    @PreAuthorize("hasAuthority('objectStorage_read')")
    override fun getCurrentActor(actorId: UUID?): Actor {

        // 1. If an ID was provided manually, use it
        if (actorId != null) {
            return actorRepository.findById(actorId).orElseThrow {
                RuntimeException("Provided Actor ID $actorId not found.")
            }
        }

        // 2. Fallback: If no ID is provided, just pick the first one (for testing only)
        return actorRepository.findAll().firstOrNull()
            ?: throw RuntimeException("No Actors found in database.")
    }
}