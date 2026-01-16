package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.AssetMapper
import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
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

    // this creates both asset and maintenance_schedule
    @Transactional
    @PreAuthorize("hasAuthority('asset_create')")
    override fun createAsset(body: AssetController.AssetPostDTO): AssetController.AssetTableDTO {
        val branchRef = branchRepository.getReferenceById(body.branchId)
        val assetCategoryRef = assetCategoryRepository.getReferenceById(body.assetCategoryId)

        val asset = assetMapper.assetDTOToAsset(body).apply {
            branch = branchRef
            assetCategory = assetCategoryRef
            maintenanceSchedule = MaintenanceSchedule().apply {
                startDate = body.maintenanceSchedule.startDate
                intervals = body.maintenanceSchedule.intervals
                intervalCount = body.maintenanceSchedule.intervalCount
            }
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
            assetObjects = body.objectIds
                .map { objectStorageRepository.getReferenceById(it) }
                .toMutableSet()
        }

        val saved = assetRepository.saveAndFlush(asset)
        return assetMapper.assetToDTO(saved)
    }

    // this is just to update asset details
    @Transactional
    @PreAuthorize("hasAuthority('asset_update')")
    override fun updateAsset(id: UUID, body: AssetController.AssetPutDTO): AssetController.AssetTableDTO {
        val asset = assetRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }.apply {
            assetMapper.assetPutDTOToAsset(body, this)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
            assetObjects.clear()
            assetObjects.addAll(
                body.objectIds
                    .map { objectStorageRepository.getReferenceById(it) }
            )
        }

        assetRepository.saveAndFlush(asset)
        return assetMapper.assetToDTO(asset)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssets(): List<AssetController.AssetTableDTO> {
        val assets = assetRepository.findAllBy()
        return assetMapper.assetsToDTO(assets)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssetById(id: UUID): AssetController.AssetTableDTO {
        val asset = assetRepository.findWithMaintenanceScheduleById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }
        return assetMapper.assetToDTO(asset)
    }

    // this deletes both asset and its maintenance_schedule
    @Transactional
    @PreAuthorize("hasAuthority('asset_delete')")
    override fun deleteAsset(id: UUID) {
        val asset = assetRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }
        val schedule = asset.maintenanceSchedule
        assetRepository.delete(asset)
        maintenanceScheduleRepository.delete(schedule)
    }
}
