package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.AssetMaintenanceMapper
import com.gms.backend.domain.application.mapper.asset.AssetMapper
import com.gms.backend.domain.application.mapper.asset.MaintenanceScheduleMapper
import com.gms.backend.domain.application.rest.asset.AssetController
import com.gms.backend.domain.application.rest.asset.AssetMaintenanceController
import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import com.gms.backend.domain.application.rest.member.report.ReportController
import com.gms.backend.domain.domain.model.asset.Asset
import com.gms.backend.domain.domain.repository.asset.AssetCategoryRepository
import com.gms.backend.domain.domain.repository.asset.AssetMaintenanceRepository
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.domain.repository.asset.BrandRepository
import com.gms.backend.domain.domain.repository.asset.MaintenanceScheduleRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.AssetExpenseRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.AssetService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class AssetServiceImpl(
    private val assetRepository: AssetRepository,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val categoryRepository: AssetCategoryRepository,
    private val objectStorageRepository: ObjectStorageRepository,
    private val assetMapper: AssetMapper,
    private val maintenanceRepository: AssetMaintenanceRepository,
    private val maintenanceScheduleRepository: MaintenanceScheduleRepository,
    private val maintenanceScheduleMapper: MaintenanceScheduleMapper,
    private val maintenanceMapper: AssetMaintenanceMapper,
    private val brandRepository: BrandRepository,
    private val assetExpenseRepository: AssetExpenseRepository
) : AssetService {

    @Transactional
    @PreAuthorize("hasAuthority('asset_create')")
    override fun createAsset(body: AssetController.AssetPostDTO): AssetController.AssetSummaryDTO {
        val asset = assetMapper.assetPostDTOToAsset(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            assetCategory = categoryRepository.getReferenceById(body.assetCategoryId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)

            if (body.objectIds.isNotEmpty()) {
                val objects = objectStorageRepository.findAllById(body.objectIds)
                assetObjects.addAll(objects)
            }

            if (body.brandIds.isNotEmpty()) {
                val brands = brandRepository.findAllById(body.brandIds)
                this.brands.addAll(brands)
            }
        }
        return assetMapper.assetToDTO(assetRepository.saveAndFlush(asset))
    }

    @Transactional
    @PreAuthorize("hasAuthority('asset_update')")
    override fun updateAsset(id: UUID, body: AssetController.AssetPutDTO): AssetController.AssetSummaryDTO {
        val asset = assetRepository.findById(id).orElseThrow { NoSuchElementException("Asset not found") }
        assetMapper.assetPutDTOToAsset(body, asset)

        asset.apply {
            branch = branchRepository.getReferenceById(body.branchId)
            assetCategory = categoryRepository.getReferenceById(body.assetCategoryId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)

            // Clear existing files and add the new files
            assetObjects.clear()
            if (body.objectIds.isNotEmpty()) {
                val objects = objectStorageRepository.findAllById(body.objectIds)
                assetObjects.addAll(objects)
            }

            brands.clear()
            if (body.brandIds.isNotEmpty()) {
                val brandList = brandRepository.findAllById(body.brandIds)
                brands.addAll(brandList)
            }
        }
        return assetMapper.assetToDTO(assetRepository.saveAndFlush(asset))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssets(
        pageable: Pageable,
        name: String?,
        branchId: UUID?,
        categoryId: UUID?,
        condition: Asset.AssetCondition?,
        status: Asset.AssetStatus?,
        dateFrom: Instant?,
        dateTo: Instant?
    ): Page<AssetController.AssetTableDTO> {
        // Pass the filters to the repository
        val assets = assetRepository.findAllProjectedBy(
            pageable, name, branchId, categoryId, condition, status, dateFrom, dateTo
        )

        val assetIds = assets.content.map { it.id }
        if (assetIds.isEmpty()) return assets

        // fetch asset expenses
        val allExpenses = assetExpenseRepository.findAllByAssetIdIn(assetIds)
        val expensesByAssetId = allExpenses.groupBy(
            { it.asset.id },
            { AssetController.AssetExpenseDTO(it.id, it.amount) }
        )

        // fetch Brands
        val brandMappings = assetRepository.findAllBrandIdsByAssetIds(assetIds)
        val brandsByAssetId = brandMappings.groupBy(
            { it.assetId },
            { it.relatedId }
        )

        // fetch Objects
        val objectMappings = assetRepository.findAllObjectIdsByAssetIds(assetIds)
        val objectsByAssetId = objectMappings.groupBy(
            { it.assetId },
            { it.relatedId }
        )

        return assets.map { asset ->
            asset.apply {
                this.expenses = expensesByAssetId.getOrDefault(id, emptyList())
                brandIds = brandsByAssetId.getOrDefault(id, emptyList())
                objectIds = objectsByAssetId.getOrDefault(id, emptyList())
            }
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read')")
    override fun getAssetById(id: UUID): AssetController.AssetTableDTO {
        val asset = assetRepository.findProjectedBy(id).orElseThrow()

        // Get all object ids
        asset.brandIds = assetRepository.findBrandIdsByAssetId(id)
        asset.objectIds = assetRepository.findObjectIdsByAssetId(id)

        return asset
    }

    @Transactional
    @PreAuthorize("hasAuthority('asset_delete')")
    override fun deleteAsset(id: UUID){
        val asset = assetRepository.findById(id).orElseThrow {
            NoSuchElementException("Asset not found with ID: $id")
        }
        assetRepository.delete(asset)
    }
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read') and hasAuthority('assetMaintenance_read')")
    override fun getAssetMaintenance(assetId: UUID, pageable: Pageable): Page<AssetMaintenanceController.AssetMaintenanceTableDTO> {
        return maintenanceRepository.findAllByAssetId(assetId, pageable)
            .map { maintenanceMapper.assetMaintenanceToDTO(it) }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('asset_read') and hasAuthority('maintenanceSchedule_read')")
    override fun getAssetSchedules(assetId: UUID, pageable: Pageable): Page<MaintenanceScheduleController.ScheduleTableDTO> {
        return maintenanceScheduleRepository.findAllByAssetId(assetId, pageable)
            .map { maintenanceScheduleMapper.scheduleToDTO(it) }
    }
}
