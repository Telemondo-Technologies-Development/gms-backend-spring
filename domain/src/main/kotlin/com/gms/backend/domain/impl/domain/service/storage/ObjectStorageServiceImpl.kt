package com.gms.backend.domain.impl.domain.service.storage

import com.gms.backend.domain.application.rest.storage.ObjectStorageController
import com.gms.backend.domain.domain.model.storage.ObjectStorage
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
        body: ObjectStorageController.ObjectStorageUploadDTO
    ): ObjectStorageController.ObjectStorageResponseDTO {
        val fileExtension = body.file.originalFilename?.substringAfterLast('.', "bin")
        val fileKey = "${body.folder}/${UUID.randomUUID()}.$fileExtension"

            body.file.inputStream.use { inputStream ->
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(body.bucket)
                        .`object`(fileKey)
                        .stream(inputStream, body.file.size, -1)
                        .contentType(body.file.contentType ?: "application/octet-stream")
                        .build()
                )
            }

            val storageRecord = ObjectStorage().apply {
                this.bucket = body.bucket
                this.fileKey = fileKey
                this.name = body.file.originalFilename ?: "unnamed"
                this.fileSize = body.file.size.toString()
                this.mimeType = body.file.contentType ?: "application/octet-stream"
                this.tags = body.folder // This sets the tag to 'expense', 'profile', etc.
                this.createdBy = actorRepository.getReferenceById(UUID.fromString(body.actorId))
                this.updatedBy = actorRepository.getReferenceById(UUID.fromString(body.actorId))
                this.status = 1
            }
        val savedRecord = objectStorageRepository.save(storageRecord)

        val url = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(savedRecord.bucket)
                .`object`(savedRecord.fileKey)
                .expiry(7, TimeUnit.DAYS) // can set this from 1 minute to 7 days
                .build()
        )
        return ObjectStorageController.ObjectStorageResponseDTO(
            id = savedRecord.id,
            name = savedRecord.name,
            url = url
        )
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
}