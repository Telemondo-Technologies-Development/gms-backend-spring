package com.gms.backend.domain.domain.repository.storage

import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ObjectStorageRepository : JpaRepository<ObjectStorage, UUID>
