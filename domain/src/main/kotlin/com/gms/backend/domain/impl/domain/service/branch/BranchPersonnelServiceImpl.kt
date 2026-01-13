package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.branch.BranchPersonnelMapper
import com.gms.backend.domain.application.rest.branch.BranchPersonnelController
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.branch.BranchPersonnelService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class BranchPersonnelServiceImpl(
    private val branchPersonnelRepository: BranchPersonnelRepository,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val branchPersonnelMapper: BranchPersonnelMapper
) : BranchPersonnelService {

    @Transactional
    @PreAuthorize("hasAuthority('branchPersonnel_create')")
    override fun createBranchPersonnel(
        body: BranchPersonnelController.BranchPersonnelPostDTO
    ): BranchPersonnelController.BranchPersonnelTableDTO {

        val actorRef = actorRepository.getReferenceById(body.actorId)
        val branchRef = branchRepository.getReferenceById(body.branchId)
        val actionActorRef = actorRepository.getReferenceById(body.createdById)

        val branchPersonnel = branchPersonnelMapper.branchPersonnelDTOToBranchPersonnel(body).apply {
            actor = actorRef
            branch = branchRef
            createdBy = actionActorRef
            updatedBy = actionActorRef
        }

        val saved = branchPersonnelRepository.save(branchPersonnel)
        return branchPersonnelMapper.branchPersonnelToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('branchPersonnel_update')")
    override fun updateBranchPersonnel(
        id: UUID,
        body: BranchPersonnelController.BranchPersonnelPutDTO
    ): BranchPersonnelController.BranchPersonnelTableDTO {
        val branchPersonnel = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }.apply {
            branchPersonnelMapper.branchPersonnelPutDTOToBranchPersonnel(body, this)
            actor = actorRepository.getReferenceById(body.actorId)
            branch = branchRepository.getReferenceById(body.branchId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        branchPersonnelRepository.save(branchPersonnel)
        return branchPersonnelMapper.branchPersonnelToDTO(branchPersonnel)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('branchPersonnel_read')")
    override fun getBranchPersonnel(): List<BranchPersonnelController.BranchPersonnelTableDTO> {
        val rows = branchPersonnelRepository.findAll()
        return branchPersonnelMapper.branchPersonnelsToDTO(rows)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('branchPersonnel_read')")
    override fun getBranchPersonnelById(id: UUID): BranchPersonnelController.BranchPersonnelTableDTO {
        val row = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }
        return branchPersonnelMapper.branchPersonnelToDTO(row)
    }

    @Transactional
    @PreAuthorize("hasAuthority('branchPersonnel_delete')")
    override fun deleteBranchPersonnel(id: UUID) {
        val bp = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }
        branchPersonnelRepository.delete(bp)
    }

}
