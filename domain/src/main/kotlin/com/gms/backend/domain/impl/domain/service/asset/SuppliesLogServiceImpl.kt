package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.SuppliesLogMapper
import com.gms.backend.domain.application.rest.asset.SuppliesLogController
import com.gms.backend.domain.domain.repository.asset.SuppliesLogRepository
import com.gms.backend.domain.domain.repository.asset.SupplyRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.SuppliesLogService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class SuppliesLogServiceImpl(
    private val suppliesLogRepository: SuppliesLogRepository,
    private val supplyRepository: SupplyRepository,
    private val actorRepository: ActorRepository,
    private val suppliesLogMapper: SuppliesLogMapper,
    private val objectStorageRepository: ObjectStorageRepository
) : SuppliesLogService {

    @Transactional
    @PreAuthorize("hasAuthority('suppliesLog_create')")
    override fun createSuppliesLog(body: SuppliesLogController.SuppliesLogPostDTO): SuppliesLogController.SuppliesLogTableDTO {
        val supplyRef = supplyRepository.getReferenceById(body.suppliesId)
        val actionActorRef = actorRepository.getReferenceById(body.createdById)

        val suppliesLog = suppliesLogMapper.suppliesLogPostDTOToSuppliesLog(body).apply {
            supplies = supplyRef
            createdBy = actionActorRef
            updatedBy = actionActorRef

            if (body.objectIds.isNotEmpty()) {
                val objects = objectStorageRepository.findAllById(body.objectIds)
                suppliesLogObjects.addAll(objects)
            }
        }

        val saved = suppliesLogRepository.saveAndFlush(suppliesLog)
        return suppliesLogMapper.suppliesLogToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('suppliesLog_update')")
    override fun updateSuppliesLog(id: UUID, body: SuppliesLogController.SuppliesLogPutDTO): SuppliesLogController.SuppliesLogTableDTO {
        val suppliesLog = suppliesLogRepository.findById(id).orElseThrow {
            NoSuchElementException("Supplies Log not found with ID: $id")
        }.apply {
            suppliesLogMapper.suppliesLogPutDTOToSuppliesLog(body, this)
            supplies = supplyRepository.getReferenceById(body.suppliesId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)

            suppliesLogObjects.clear()
            if (body.objectIds.isNotEmpty()) {
                val objects = objectStorageRepository.findAllById(body.objectIds)
                suppliesLogObjects.addAll(objects)
            }
        }

        suppliesLogRepository.saveAndFlush(suppliesLog)
        return suppliesLogMapper.suppliesLogToDTO(suppliesLog)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('suppliesLog_read')")
    override fun getSuppliesLogs(pageable: Pageable): Page<SuppliesLogController.SuppliesLogTableDTO> {
        return suppliesLogRepository.findAll(pageable)
            .map { log -> suppliesLogMapper.suppliesLogToDTO(log) }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('suppliesLog_read')")
    override fun getSuppliesLogById(id: UUID): SuppliesLogController.SuppliesLogTableDTO {
        val log = suppliesLogRepository.findById(id).orElseThrow {
            NoSuchElementException("Supplies Log not found with ID: $id")
        }
        return suppliesLogMapper.suppliesLogToDTO(log)
    }

    @Transactional
    @PreAuthorize("hasAuthority('suppliesLog_delete')")
    override fun deleteSuppliesLog(id: UUID) {
        val log = suppliesLogRepository.findById(id).orElseThrow {
            NoSuchElementException("Supplies Log not found with ID: $id")
        }
        suppliesLogRepository.delete(log)
    }
}