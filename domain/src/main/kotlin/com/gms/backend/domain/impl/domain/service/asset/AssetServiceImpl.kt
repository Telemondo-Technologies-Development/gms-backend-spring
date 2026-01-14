package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.AssetMapper
import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.repository.asset.AssetCategoryRepository
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.domain.repository.asset.MaintenanceScheduleRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.AssetService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.util.UUID

@Service
@PreAuthorize("denyAll()")
class AssetServiceImpl(
    private val assetMapper: AssetMapper,
    private val assetRepository: AssetRepository,
    private val actorRepository: ActorRepository,
    private val objectStorageRepository: ObjectStorageRepository,
    private val branchRepository: BranchRepository,
    private val maintenanceScheduleRepository: MaintenanceScheduleRepository,
    private val assetCategoryRepository: AssetCategoryRepository
): AssetService {
    @Transactional
    @PreAuthorize("hasAuthority('asset_create')")
    override fun createAsset(body: AssetController.AssetPostDTO): AssetController.AssetTableDTO {
        val branchRef = branchRepository.getReferenceById(body.branchId)
        val maintenanceScheduleRef = maintenanceScheduleRepository.getReferenceById(body.maintenanceScheduleId)
        val assetCategoryRef = assetCategoryRepository.getReferenceById(body.assetCategoryId)

        val asset = assetMapper.assetDTOToAsset(body).apply {
            branch = branchRef
            maintenanceSchedule = maintenanceScheduleRef
            assetCategory = assetCategoryRef
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
            assetObjects = body.objectIds
                .map { objectStorageRepository.getReferenceById(it) }
                .toMutableSet()
        }

        val saved = assetRepository.save(asset)
        return assetMapper.assetToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('asset_update')")
    override fun updateAsset(id: UUID, body: AssetController.AssetPutDTO): AssetController.AssetTableDTO {
        val asset = assetRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }.apply {
            assetMapper.assetPutDTOToAsset(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
            assetObjects.clear()
            assetObjects.addAll(
                body.objectIds
                    .map { objectStorageRepository.getReferenceById(it) }
            )
        }

        assetRepository.save(asset)
        return assetMapper.assetToDTO(asset)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssets(): List<AssetController.AssetTableDTO> {
        val assets = assetRepository.findAll()
        return assetMapper.assetsToDTO(assets)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssetById(id: UUID): AssetController.AssetTableDTO {
        val branch = assetRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }
        return assetMapper.assetToDTO(branch)
    }

    @Transactional
    @PreAuthorize("hasAuthority('asset_delete')")
    override fun deleteAsset(id: UUID) {
        val branch = assetRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }
        assetRepository.delete(branch)
    }


}
