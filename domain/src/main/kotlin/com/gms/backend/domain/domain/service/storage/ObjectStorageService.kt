package com.gms.backend.domain.domain.service.storage

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface ObjectStorageService  {
    fun uploadFile(
        file: MultipartFile,
        bucket: String,
        folder: String, // Added this
        actor: Actor
    ): ObjectStorage
    fun getDownloadUrl(id: UUID): String
    fun deleteFile(id: UUID)
}
