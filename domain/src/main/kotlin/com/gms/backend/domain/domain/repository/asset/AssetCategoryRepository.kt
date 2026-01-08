package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.AssetCategory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AssetCategoryRepository : JpaRepository<AssetCategory, UUID>
