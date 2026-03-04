package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.domain.model.asset.Brand
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BrandRepository : JpaRepository<Brand, UUID>{
}
