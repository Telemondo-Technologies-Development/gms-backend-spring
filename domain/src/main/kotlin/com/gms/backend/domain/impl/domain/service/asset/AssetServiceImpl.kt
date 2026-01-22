package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.AssetMapper
import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.repository.asset.AssetCategoryRepository
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.AssetService
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Pageable
import java.util.NoSuchElementException
import java.util.UUID

@Service
@PreAuthorize("denyAll()")
class AssetServiceImpl(
    private val assetRepository: AssetRepository,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val categoryRepository: AssetCategoryRepository,
    private val assetMapper: AssetMapper
) : AssetService {

    @Transactional
    @PreAuthorize("hasAuthority('asset_create')")
    override fun createAsset(body: AssetController.AssetPostDTO): AssetController.AssetTableDTO {
        val asset = assetMapper.assetPostDTOToAsset(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            assetCategory = categoryRepository.getReferenceById(body.assetCategoryId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }
        return assetMapper.assetToDTO(assetRepository.saveAndFlush(asset))
    }

    @Transactional
    @PreAuthorize("hasAuthority('asset_update')")
    override fun updateAsset(id: UUID, body: AssetController.AssetPutDTO): AssetController.AssetTableDTO {
        val asset = assetRepository.findById(id).orElseThrow { NoSuchElementException("Asset not found") }
        assetMapper.assetPutDTOToAsset(body, asset)
        asset.apply {
            branch = branchRepository.getReferenceById(body.branchId)
            assetCategory = categoryRepository.getReferenceById(body.assetCategoryId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }
        return assetMapper.assetToDTO(assetRepository.saveAndFlush(asset))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssets(pageable: Pageable): Page<AssetController.AssetTableDTO> =
        assetRepository.findAll(pageable).map { asset ->
            assetMapper.assetToDTO(asset)
        }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssetById(id: UUID) = assetMapper.assetToDTO(assetRepository.findById(id).get())

    @Transactional
    @PreAuthorize("hasAuthority('asset_delete')")
    override fun deleteAsset(id: UUID){
        val asset = assetRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }
        assetRepository.delete(asset)
    }
    @Transactional
    @PreAuthorize("hasAuthority('asset_create')")
    override fun linkObjectToAsset(assetId: UUID, objectStorage: ObjectStorage) {
        val asset = assetRepository.findById(assetId).orElseThrow { Exception("Asset not found") }
        asset.assetObjects.add(objectStorage)
        assetRepository.save(asset)
    }

}
