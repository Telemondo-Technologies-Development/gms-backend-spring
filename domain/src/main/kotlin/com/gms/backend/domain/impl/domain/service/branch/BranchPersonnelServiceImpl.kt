package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.BranchPersonnelMapper
import com.gms.backend.domain.application.rest.BranchPersonnelController
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.branch.BranchPersonnelService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class BranchPersonnelServiceImpl(
    private val branchPersonnelRepository: BranchPersonnelRepository,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val branchPersonnelMapper: BranchPersonnelMapper
) : BranchPersonnelService {

    override fun createBranchPersonnel(
        body: BranchPersonnelController.BranchPersonnelPostDTO
    ): BranchPersonnelController.BranchPersonnelTableDTO {

        val actor = actorRepository.findById(body.actorId)
            .orElseThrow { NoSuchElementException("Actor not found: ${body.actorId}") }

        val branch = branchRepository.findById(body.branchId)
            .orElseThrow { NoSuchElementException("Branch not found: ${body.branchId}") }

        val actionActor = actorRepository.findById(body.createdById)
            .orElseThrow { NoSuchElementException("CreatedBy actor not found: ${body.createdById}") }

        val branchPersonnel = branchPersonnelMapper.branchPersonnelDTOToBranchPersonnel(body).apply {
            this.actor = actor
            this.branch = branch
            this.createdBy = actionActor
            this.updatedBy = actionActor
        }

        val saved = branchPersonnelRepository.saveAndFlush(branchPersonnel)
        val reloaded = branchPersonnelRepository.findById(saved.id).orElseThrow()
        return branchPersonnelMapper.branchPersonnelToDTO(reloaded)
    }

    override fun updateBranchPersonnel(
        id: UUID,
        body: BranchPersonnelController.BranchPersonnelPutDTO
    ): BranchPersonnelController.BranchPersonnelTableDTO {

        val branchPersonnel = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }

        branchPersonnelMapper.branchPersonnelPutDTOToBranchPersonnel(body, branchPersonnel)
        branchPersonnel.id = id

        branchPersonnel.actor = actorRepository.getReferenceById(body.actorId)
        branchPersonnel.branch = branchRepository.getReferenceById(body.branchId)

        val updatedByRef = actorRepository.getReferenceById(body.updatedById)
        branchPersonnel.updatedBy = updatedByRef

        val saved = branchPersonnelRepository.saveAndFlush(branchPersonnel)
        val reloaded = branchPersonnelRepository.findById(saved.id).orElseThrow()
        return branchPersonnelMapper.branchPersonnelToDTO(reloaded)
    }

    override fun getBranchPersonnel(): List<BranchPersonnelController.BranchPersonnelTableDTO> {
        val rows = branchPersonnelRepository.findAll()
        return branchPersonnelMapper.branchPersonnelsToDTO(rows)
    }

    override fun getBranchPersonnelById(id: UUID): BranchPersonnelController.BranchPersonnelTableDTO {
        val row = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }
        return branchPersonnelMapper.branchPersonnelToDTO(row)
    }

    override fun deleteBranchPersonnel(id: UUID) {
        val bp = branchPersonnelRepository.findById(id).orElseThrow {
            NoSuchElementException("BranchPersonnel not found with ID: $id")
        }
        branchPersonnelRepository.delete(bp)
    }

}
