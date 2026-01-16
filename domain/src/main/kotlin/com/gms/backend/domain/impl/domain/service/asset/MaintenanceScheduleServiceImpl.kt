package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.MaintenanceScheduleMapper
import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.MaintenanceScheduleService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.util.UUID

@Service
@PreAuthorize("denyAll()")
class MaintenanceScheduleServiceImpl(
    private val assetRepository: AssetRepository,
    private val actorRepository: ActorRepository,
    private val maintenanceScheduleMapper: MaintenanceScheduleMapper
) : MaintenanceScheduleService {

    @Transactional
    @PreAuthorize("hasAuthority('maintenanceSchedule_update')")
    override fun updateMaintenanceSchedule(
        assetId: UUID,
        body: MaintenanceScheduleController.MaintenanceSchedulePutDTO
    ): MaintenanceScheduleController.MaintenanceScheduleTableDTO {

        val asset = assetRepository.findById(assetId).orElseThrow {
            NoSuchElementException("Asset not found with ID: $assetId")
        }.apply {
            maintenanceSchedule.apply {
                maintenanceScheduleMapper.maintenanceSchedulePutDTOToMaintenanceSchedule(body, this)
            }
        }

        assetRepository.saveAndFlush(asset)
        return maintenanceScheduleMapper.maintenanceScheduleToDTO(asset.maintenanceSchedule)
    }
}
