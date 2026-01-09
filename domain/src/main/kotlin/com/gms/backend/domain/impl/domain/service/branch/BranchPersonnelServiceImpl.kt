package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.BranchPersonnelMapper
import com.gms.backend.domain.application.rest.BranchPersonnelController
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.branch.BranchPersonnelService
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BranchPersonnelServiceImpl(
    private val branchPersonnelRepository: BranchPersonnelRepository,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val branchPersonnelMapper: BranchPersonnelMapper
) : BranchPersonnelService {

    @Transactional
    override fun createBranchPersonnel(
        body: BranchPersonnelController.BranchPersonnelPostDTO
    ): BranchPersonnelController.BranchPersonnelTableDTO {

        val actorRef = actorRepository.getReferenceById(body.actorId)
        val branchRef = branchRepository.getReferenceById(body.branchId)
        val actionActorRef = actorRepository.getReferenceById(body.createdById)

        val branchPersonnel = branchPersonnelMapper.branchPersonnelDTOToBranchPersonnel(body).apply {
            this.actor = actorRef
            this.branch = branchRef
            this.createdBy = actionActorRef
            this.updatedBy = actionActorRef
        }

        val saved = branchPersonnelRepository.saveAndFlush(branchPersonnel)
        val reloaded = branchPersonnelRepository.findById(saved.id).orElseThrow()
        return branchPersonnelMapper.branchPersonnelToDTO(reloaded)
    }

    @Transactional
    override fun updateBranchPersonnel(
        id: UUID,
        body: BranchPersonnelController.BranchPersonnelPutDTO
    ): BranchPersonnelController.BranchPersonnelTableDTO {

        val branchPersonnel = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }

        branchPersonnelMapper.branchPersonnelPutDTOToBranchPersonnel(body, branchPersonnel)

        branchPersonnel.actor = actorRepository.getReferenceById(body.actorId)
        branchPersonnel.branch = branchRepository.getReferenceById(body.branchId)

        val updatedByRef = actorRepository.getReferenceById(body.updatedById)
        branchPersonnel.updatedBy = updatedByRef

        val saved = branchPersonnelRepository.saveAndFlush(branchPersonnel)
        val reloaded = branchPersonnelRepository.findById(saved.id).orElseThrow()
        return branchPersonnelMapper.branchPersonnelToDTO(reloaded)
    }

    @Transactional(readOnly = true)
    override fun getBranchPersonnel(): List<BranchPersonnelController.BranchPersonnelTableDTO> {
        val rows = branchPersonnelRepository.findAll()
        return branchPersonnelMapper.branchPersonnelsToDTO(rows)
    }

    @Transactional(readOnly = true)
    override fun getBranchPersonnelById(id: UUID): BranchPersonnelController.BranchPersonnelTableDTO {
        val row = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }
        return branchPersonnelMapper.branchPersonnelToDTO(row)
    }

    @Transactional
    override fun deleteBranchPersonnel(id: UUID) {
        val bp = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }
        branchPersonnelRepository.delete(bp)
    }

}
