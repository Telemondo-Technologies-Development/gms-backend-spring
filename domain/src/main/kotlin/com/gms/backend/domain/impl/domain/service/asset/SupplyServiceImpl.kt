package com.gms.backend.domain.impl.domain.service.asset

import com.gms.backend.domain.application.mapper.asset.SupplyMapper
import com.gms.backend.domain.application.rest.asset.SupplyController
import com.gms.backend.domain.domain.repository.asset.SupplyRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.asset.SupplyService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class SupplyServiceImpl(
    private val supplyRepository: SupplyRepository,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val supplyMapper: SupplyMapper,
    private val objectStorageRepository: ObjectStorageRepository,
) : SupplyService {

    @Transactional
    @PreAuthorize("hasAuthority('supply_create')")
    override fun createSupply(body: SupplyController.SupplyPostDTO): SupplyController.SupplyTableDTO {
        val branchRef = branchRepository.getReferenceById(body.branchId)
        val actionActorRef = actorRepository.getReferenceById(body.createdById)

        val supply = supplyMapper.supplyPostDTOToSupply(body).apply {
            branch = branchRef
            createdBy = actionActorRef
            updatedBy = actionActorRef

            if (body.objectIds.isNotEmpty()) {
                val objects = objectStorageRepository.findAllById(body.objectIds)
                suppliesObjects.addAll(objects)
            }
        }

        val saved = supplyRepository.saveAndFlush(supply)
        return supplyMapper.supplyToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('supply_update')")
    override fun updateSupply(id: UUID, body: SupplyController.SupplyPutDTO): SupplyController.SupplyTableDTO {
        val supply = supplyRepository.findById(id).orElseThrow {
            NoSuchElementException("Supply not found with ID: $id")
        }.apply {
            supplyMapper.supplyPutDTOToSupply(body, this)
            branch = branchRepository.getReferenceById(body.branchId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)

            suppliesObjects.clear()
            if (body.objectIds.isNotEmpty()) {
                val objects = objectStorageRepository.findAllById(body.objectIds)
                suppliesObjects.addAll(objects)
            }
        }

        supplyRepository.saveAndFlush(supply)
        return supplyMapper.supplyToDTO(supply)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('supply_read')")
    override fun getSupplies(pageable: Pageable): Page<SupplyController.SupplyTableDTO> {
        return supplyRepository.findAll(pageable)
            .map { supply -> supplyMapper.supplyToDTO(supply) }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('supply_read')")
    override fun getSupplyById(id: UUID): SupplyController.SupplyTableDTO {
        val supply = supplyRepository.findById(id).orElseThrow {
            NoSuchElementException("Supply not found with ID: $id")
        }
        return supplyMapper.supplyToDTO(supply)
    }

    @Transactional
    @PreAuthorize("hasAuthority('supply_delete')")
    override fun deleteSupply(id: UUID) {
        val supply = supplyRepository.findById(id).orElseThrow {
            NoSuchElementException("Supply not found with ID: $id")
        }
        supplyRepository.delete(supply)
    }
}