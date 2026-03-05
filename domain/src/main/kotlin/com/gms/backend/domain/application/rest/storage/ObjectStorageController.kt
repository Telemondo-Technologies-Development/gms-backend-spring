package com.gms.backend.domain.application.rest.storage

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.service.storage.ObjectStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/storage")
@Tag(name = "Object Storage")
class ObjectStorageController(
    private val storageService: ObjectStorageService,
    private val bucketConfig: MinioBucketConfig
) {
    @Configuration
    @ConfigurationProperties(prefix = "minio.bucket")
    data class MinioBucketConfig(
        var public: String = "",
        var private: String = ""
    )
    data class ObjectStorageDTO(
        val id: UUID,
        val name: String,
        val fileKey: String,
        val mimeType: String,
        val fileSize: String,
        val bucket: String
    )
    @GetMapping("/{id}/url")
    @Operation(summary = "Get a file from the object storage by ID")
    fun getUrl(@PathVariable id: UUID) =
            storageService.getDownloadUrl(id).toOkResponse("URL generated")

    @PostMapping("/upload")
    @Operation(summary = "Upload a file into the object storage")
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("bucket") bucket: String,
        @RequestParam("path") path: String
    ) = storageService.uploadFile(file, bucket, path,storageService.getCurrentActor()).toCreatedResponse("File uploaded")

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a file from object storage and database")
    fun deleteFile(@PathVariable id: UUID) =
        storageService.deleteFile(id).toOkResponse("file deleted from database and object storage")

    // --- 2. EXPENSES & BILLING (Private Bucket) ---

    @PostMapping("/upload/expense/receipt")
    @Operation(summary = "(private)")
    fun uploadExpenseReceipt(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("category") category: String // utility, salary, asset, etc.
    ) =
        storageService.uploadFile(file, bucketConfig.private, "expenses/$category", storageService.getCurrentActor()).toCreatedResponse("Receipt for $category uploaded successfully")

    @PostMapping("/upload/payment-method/doc")
    @Operation(summary = "(private)")
    fun uploadPaymentMethodDoc(@RequestParam("file") file: MultipartFile) =
        storageService.uploadFile(file, bucketConfig.private, "billing/payment-methods", storageService.getCurrentActor()).toCreatedResponse("Payment method document saved")

    // --- 3. ASSETS, SUPPLIES, AND MAINTENANCE (Private Bucket) ---

    @PostMapping("/upload/asset/document")
    @Operation(summary = "(private)")
    fun uploadAssetDocument(@RequestParam("file") file: MultipartFile) =
        storageService.uploadFile(file, bucketConfig.private, "assets/documents", storageService.getCurrentActor()).toCreatedResponse("Asset document uploaded")

    @PostMapping("/upload/asset/maintenance")
    @Operation(summary = "(private)")
    fun uploadMaintenanceRecord(@RequestParam("file") file: MultipartFile) =
        storageService.uploadFile(file, bucketConfig.private, "assets/maintenance", storageService.getCurrentActor()).toCreatedResponse("Maintenance record saved")

    @PostMapping("/upload/brand/logo")
    @Operation(summary = "(private)")
    fun uploadBrandLogo(@RequestParam("file") file: MultipartFile) =
        storageService.uploadFile(file, bucketConfig.private, "brand/logo", storageService.getCurrentActor()).toCreatedResponse("Brand logo uploaded")

    @PostMapping("/upload/supply/document")
    @Operation(summary = "(private)")
    fun uploadSupplyPhoto(@RequestParam("file") file: MultipartFile) =
        storageService.uploadFile(file, bucketConfig.private, "supplies/documents", storageService.getCurrentActor()).toCreatedResponse("Supply photo uploaded")

    @PostMapping("/upload/supply/log")
    @Operation(summary = "(private)")
    fun uploadSupplyLogAttachment(@RequestParam("file") file: MultipartFile) =
        storageService.uploadFile(file, bucketConfig.private, "supplies/logs", storageService.getCurrentActor()).toCreatedResponse("Log attachment saved")

    // --- 4. REPORTS (Private Bucket) ---

    @PostMapping("/upload/report/attachment")
    @Operation(summary = "(private)")
    fun uploadReportAttachment(@RequestParam("file") file: MultipartFile) =
        storageService.uploadFile(file, bucketConfig.private, "reports/attachments", storageService.getCurrentActor()).toCreatedResponse("Report attachment uploaded")

    private fun convertToDTO(entity: ObjectStorage): ObjectStorageDTO {
        return ObjectStorageDTO(
            id = entity.id,
            name = entity.name,
            fileKey = entity.fileKey,
            mimeType = entity.mimeType,
            fileSize = entity.fileSize,
            bucket = entity.bucket
        )
    }
}