package com.gms.backend.domain.domain.service.storage

import com.gms.backend.domain.application.rest.storage.ObjectStorageController
import java.util.*

interface ObjectStorageService  {
    fun uploadFile(
        body: ObjectStorageController.ObjectStorageUploadDTO
    ): ObjectStorageController.ObjectStorageResponseDTO
    fun getDownloadUrl(id: UUID): String
    fun deleteFile(id: UUID)
}
