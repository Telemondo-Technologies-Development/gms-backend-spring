package com.gms.backend.domain.domain.service.asset

import com.gms.backend.domain.application.rest.asset.AssetCategoryController
import com.gms.backend.domain.application.rest.asset.BrandController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface BrandService {
    fun createBrand(body: BrandController.BrandPostDTO): BrandController.BrandTableDTO
    fun updateBrand(id: UUID, body: BrandController.BrandPutDTO): BrandController.BrandTableDTO
    fun getBrands(pageable: Pageable): Page<BrandController.BrandTableDTO>
    fun getBrandById(id: UUID): BrandController.BrandTableDTO
    fun deleteBrand(id: UUID)
}
