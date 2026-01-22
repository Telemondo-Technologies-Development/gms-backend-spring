package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.AssetMaintenanceMapper
import com.gms.backend.domain.application.rest.asset.AssetMaintenanceController
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.repository.asset.AssetMaintenanceRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.AssetMaintenanceService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class AssetMaintenanceServiceImpl(
    private val maintenanceRepository: AssetMaintenanceRepository,
    private val maintenanceMapper: AssetMaintenanceMapper,
    private val actorRepository: ActorRepository
) : AssetMaintenanceService {

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetMaintenance_read')")
    override fun getMaintenanceLogs(pageable: Pageable): Page<AssetMaintenanceController.AssetMaintenanceTableDTO> {
        return maintenanceRepository.findAll(pageable)
            .map { maintenanceMapper.assetMaintenanceToDTO(it) }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetMaintenance_read')")
    override fun getMaintenanceLogById(id: UUID): AssetMaintenanceController.AssetMaintenanceTableDTO {
        val row = maintenanceRepository.findById(id).orElseThrow {
            NoSuchElementException("Maintenance log not found with ID: $id")
        }
        return maintenanceMapper.assetMaintenanceToDTO(row)
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetMaintenance_update')")
    override fun updateMaintenanceStatus(
        id: UUID,
        request: AssetMaintenanceController.AssetMaintenancePatchDTO
    ): AssetMaintenanceController.AssetMaintenanceTableDTO {

        val maintenance = maintenanceRepository.findById(id).orElseThrow {
            NoSuchElementException("Maintenance log not found with ID: $id")
        }

        maintenance.status = request.status
        maintenance.description = request.description
        maintenance.completionDate = request.completionDate
        maintenance.updatedBy = actorRepository.getReferenceById(request.updatedById)
        maintenance.updatedById = request.updatedById

        val saved = maintenanceRepository.saveAndFlush(maintenance)

        return maintenanceMapper.assetMaintenanceToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetMaintenance_update')")
    override fun linkObjectToMaintenance(id: UUID, objectStorage: ObjectStorage) {
        val maintenanceLog = maintenanceRepository.findById(id)
            .orElseThrow { NoSuchElementException("Maintenance log not found with id: $id") }

        maintenanceLog.assetMaintenanceObjects.add(objectStorage)

        maintenanceRepository.save(maintenanceLog)
    }
}