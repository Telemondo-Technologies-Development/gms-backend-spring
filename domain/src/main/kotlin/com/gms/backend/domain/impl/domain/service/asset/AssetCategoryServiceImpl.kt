package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.AssetCategoryMapper
import com.gms.backend.domain.application.rest.asset.AssetCategoryController
import com.gms.backend.domain.domain.repository.asset.AssetCategoryRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.AssetCategoryService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class AssetCategoryServiceImpl (
    private val assetCategoryMapper: AssetCategoryMapper,
    private val assetCategoryRepository: AssetCategoryRepository,
    private val actorRepository: ActorRepository
): AssetCategoryService {
    @Transactional
    @PreAuthorize("hasAuthority('assetCategory_create')")
    override fun createAssetCategory(body: AssetCategoryController.AssetCategoryPostDTO): AssetCategoryController.AssetCategoryTableDTO {
        val assetCategory = assetCategoryMapper.assetCategoryDTOToAssetCategory(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = assetCategoryRepository.saveAndFlush(assetCategory)
        return assetCategoryMapper.assetCategoryToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetCategory_update')")
    override fun updateAssetCategory(id: UUID, body: AssetCategoryController.AssetCategoryPutDTO): AssetCategoryController.AssetCategoryTableDTO{
        val assetCategory = assetCategoryRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset Category not found with ID: $id")
        }.apply {
            assetCategoryMapper.assetCategoryPutDTOToAssetCategory(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        assetCategoryRepository.saveAndFlush(assetCategory)
        return assetCategoryMapper.assetCategoryToDTO(assetCategory)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetCategory_read')")
    override fun getAssetCategories(): List<AssetCategoryController.AssetCategoryTableDTO>{
        val assetCategories = assetCategoryRepository.findAll()
        return assetCategoryMapper.assetCategoriesToDTO(assetCategories)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetCategory_read')")
    override fun getAssetCategoryById(id: UUID): AssetCategoryController.AssetCategoryTableDTO{
        val assetCategory = assetCategoryRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset Category not found with ID: $id")
        }
        return assetCategoryMapper.assetCategoryToDTO(assetCategory)
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetCategory_delete')")
    override fun deleteAssetCategory(id: UUID){
        val assetCategory = assetCategoryRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset Category not found with ID: $id")
        }
        assetCategoryRepository.delete(assetCategory)
    }

}
