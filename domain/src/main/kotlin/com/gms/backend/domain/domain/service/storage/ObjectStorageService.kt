package com.gms.backend.domain.domain.service.storage

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Actor
import org.springframework.web.multipart.MultipartFile

interface ObjectStorageService  {
    fun uploadPicture(file: MultipartFile, bucket: String, actor: Actor): ObjectStorage
}