package com.gms.backend.domain.application.rest.storage

import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.application.response.*
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.storage.ObjectStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/storage")
@Tag(name = "Object Storage")
class ObjectStorageController(
    private val storageService: ObjectStorageService,
    private val actorRepository: ActorRepository,
    @Value($$"${minio.bucket.public}") private val publicBucket: String,
    @Value($$"${minio.bucket.private}") private val privateBucket: String
) {
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
    fun getUrl(@PathVariable id: UUID): ResponseEntity<ApiResponse<String>> =
            storageService.getDownloadUrl(id).toOkResponse("URL generated")

    // --- 1. PROFILE PICTURES (Public Bucket) ---

    @PostMapping("/upload/member/profile")
    @Operation(summary = "Upload a member profile picture into the object storage (public)")
    fun uploadMemberProfile(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, publicBucket, "profiles/members", getCurrentActor()).toCreatedResponse("Member profile picture uploaded successfully")


    @PostMapping("/upload/employee/profile")
    @Operation(summary = "Upload an employee profile picture into the object storage (public)")
    fun uploadEmployeeProfile(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, publicBucket, "profiles/employees", getCurrentActor()).toCreatedResponse("Employee profile picture uploaded successfully")

    @PostMapping("/upload/branch/profile")
    @Operation(summary = "Upload a branch logo into the object storage (public)")
    fun uploadBranchLogo(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, publicBucket, "profiles/branches", getCurrentActor()).toCreatedResponse("Branch logo uploaded successfully")


    // --- 2. EXPENSES & BILLING (Private Bucket) ---

    @PostMapping("/upload/expense/receipt")
    @Operation(summary = "(private)")
    fun uploadExpenseReceipt(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("category") category: String // utility, salary, asset, etc.
    ): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, privateBucket, "expenses/$category", getCurrentActor()).toCreatedResponse("Receipt for $category uploaded successfully")

    @PostMapping("/upload/payment-method/doc")
    @Operation(summary = "(private)")
    fun uploadPaymentMethodDoc(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, privateBucket, "billing/payment-methods", getCurrentActor()).toCreatedResponse("Payment method document saved")

    // --- 3. ASSETS & MAINTENANCE (Private Bucket) ---

    @PostMapping("/upload/asset/document")
    @Operation(summary = "(private)")
    fun uploadAssetDocument(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, privateBucket, "assets/documents", getCurrentActor()).toCreatedResponse("Asset document uploaded")

    @PostMapping("/upload/asset/maintenance")
    @Operation(summary = "(private)")
    fun uploadMaintenanceRecord(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, privateBucket, "assets/maintenance", getCurrentActor()).toCreatedResponse("Maintenance record saved")

    // --- 4. REPORTS (Private Bucket) ---

    @PostMapping("/upload/report/attachment")
    @Operation(summary = "(private)")
    fun uploadReportAttachment(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, privateBucket, "reports/attachments", getCurrentActor()).toCreatedResponse("Report attachment uploaded")

    // DELETE

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a file from object storage and database")
    fun deleteFile(@PathVariable id: UUID): ResponseEntity<ApiResponse<Unit>> =
            storageService.deleteFile(id).toOkResponse("file deleted from database and object storage")

    private fun getCurrentActor(actorId: UUID? = null): Actor {

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