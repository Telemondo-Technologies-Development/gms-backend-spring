package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.BranchMapper
import com.gms.backend.domain.application.rest.BranchController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.branch.BranchService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.Optional

@Service
class BranchServiceImpl(
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
    private val objectStorageRepository: ObjectStorageRepository,
    private val branchMapper: BranchMapper,
) : BranchService {

    @Transactional
    override fun createBranch(body: BranchController.BranchPostDTO): BranchController.BranchTableDTO {
        val actionActorRef = actorRepository.getReferenceById(body.createdById)

        val branch = branchMapper.branchDTOToBranch(body).apply {
            createdBy = actionActorRef
            updatedBy = actionActorRef
        }

        body.profilePictureObjectId?.let {
            branch.profilePicture = objectStorageRepository.getReferenceById(it)
        }

        val saved = branchRepository.saveAndFlush(branch)
        val reloaded = branchRepository.findById(saved.id).orElseThrow()
        return branchMapper.branchToDTO(reloaded)
    }

    @Transactional
    override fun updateBranch(id: UUID, body: BranchController.BranchPutDTO): BranchController.BranchTableDTO {
        val branch = branchRepository.findById(id).orElseThrow {
            NoSuchElementException("Branch not found with ID: $id")
        }

        branchMapper.branchPutDTOToBranch(body, branch)
        branch.id = id

        val updatedByRef = actorRepository.getReferenceById(body.updatedById)
        branch.updatedBy = updatedByRef


        body.profilePictureObjectId?.let {
            branch.profilePicture = objectStorageRepository.getReferenceById(it)
        }

        return branchMapper.branchToDTO(branch)
    }

    @Transactional(readOnly = true)
    override fun getBranches(): List<BranchController.BranchTableDTO> {
        val branches = branchRepository.findAll()
        return branchMapper.branchesToDTO(branches)
    }

    @Transactional(readOnly = true)
    override fun getBranchById(id: UUID): BranchController.BranchTableDTO {
        val branch = branchRepository.findById(id).orElseThrow {
            NoSuchElementException("Branch not found with ID: $id")
        }
        return branchMapper.branchToDTO(branch)
    }

    @Transactional
    override fun deleteBranch(id: UUID) {
        val branch = branchRepository.findById(id).orElseThrow {
            NoSuchElementException("Branch not found with ID: $id")
        }
        branchRepository.delete(branch)
    }
}
