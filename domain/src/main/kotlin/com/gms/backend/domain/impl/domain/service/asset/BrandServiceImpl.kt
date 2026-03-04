package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.domain.service.asset.BrandService
import com.gms.backend.domain.application.mapper.asset.AssetCategoryMapper
import com.gms.backend.domain.application.mapper.asset.BrandMapper
import com.gms.backend.domain.application.rest.asset.AssetCategoryController
import com.gms.backend.domain.application.rest.asset.BrandController
import com.gms.backend.domain.domain.repository.asset.AssetCategoryRepository
import com.gms.backend.domain.domain.repository.asset.BrandRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class BrandServiceImpl (
    private val brandMapper: BrandMapper,
    private val brandRepository: BrandRepository,
    private val actorRepository: ActorRepository
): BrandService {
    @Transactional
    @PreAuthorize("hasAuthority('brand_create')")
    override fun createBrand(body: BrandController.BrandPostDTO): BrandController.BrandTableDTO{
        val brand = brandMapper.brandPostDTOToBrand(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = brandRepository.saveAndFlush(brand)
        return brandMapper.brandToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('brand_update')")
    override fun updateBrand(id: UUID, body: BrandController.BrandPutDTO): BrandController.BrandTableDTO{
        val brand = brandRepository.findById(id).orElseThrow {
            NoSuchElementException("Brand not found with ID: $id")
        }.apply {
            brandMapper.brandPutDTOToBrand(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        brandRepository.saveAndFlush(brand)
        return brandMapper.brandToDTO(brand)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('brand_read')")
    override fun getBrands(pageable: Pageable): Page<BrandController.BrandTableDTO> {
        return brandRepository.findAll(pageable)
            .map { supply -> brandMapper.brandToDTO(supply) }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('brand_read')")
    override fun getBrandById(id: UUID): BrandController.BrandTableDTO{
        val log = brandRepository.findById(id).orElseThrow {
            NoSuchElementException("Brand not found with ID: $id")
        }
        return brandMapper.brandToDTO(log)
    }

    @Transactional
    @PreAuthorize("hasAuthority('brand_delete')")
    override fun deleteBrand(id: UUID){
        val log = brandRepository.findById(id).orElseThrow {
            NoSuchElementException("Brand not found with ID: $id")
        }
        brandRepository.delete(log)
    }
}
