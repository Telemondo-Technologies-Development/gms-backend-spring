package com.gms.backend.domain.application.rest.storage

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.service.storage.ObjectStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/storage")
@Tag(name = "Object Storage")
class ObjectStorageController(
    private val storageService: ObjectStorageService
) {
    data class ObjectStorageResponseDTO(
        val id: UUID,
        val name: String,
        val url: String
    )
    data class ObjectStorageUploadDTO(
        val file: MultipartFile,
        val bucket: String,
        val folder: String,
        val actorId: String,
    ) { companion object {
            fun public(file: MultipartFile, actorId: String, folder: String) =
                ObjectStorageUploadDTO(file, "gms-public", folder, actorId)

            fun private(file: MultipartFile, actorId: String, folder: String) =
                ObjectStorageUploadDTO(file, "gms-private", folder, actorId) }
    }

    @GetMapping("/{id}/url")
    @Operation(summary = "Get a file from the object storage by ID")
    fun getUrl(@PathVariable id: UUID) =
            storageService.getDownloadUrl(id).toOkResponse("URL generated")

    @PostMapping("/upload")
    @Operation(summary = "Upload a file into the object storage")
    fun uploadFile( @ModelAttribute body: ObjectStorageUploadDTO
    ) = storageService.uploadFile(body).toCreatedResponse("File uploaded")

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a file from object storage and database")
    fun deleteFile(@PathVariable id: UUID) =
        storageService.deleteFile(id).toOkResponse("file deleted from database and object storage")

    @PostMapping("/upload/expense/receipt", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Upload using only actorId and file")
    fun uploadExpenseReceipt(@RequestParam("file") file: MultipartFile, @RequestParam("actorId") actorId: String, @RequestParam("category") category: String) =
        storageService.uploadFile(ObjectStorageUploadDTO.private(file = file, actorId = actorId, folder = "expenses/$category")).toCreatedResponse("Member profile picture uploaded successfully")
}