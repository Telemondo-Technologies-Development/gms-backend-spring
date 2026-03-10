package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.branch.BranchMapper
import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.application.rest.member.MemberController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.repository.user.EmployeeRepository
import com.gms.backend.domain.domain.service.branch.BranchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class BranchServiceImpl(
    private val branchRepository: BranchRepository,
    private val branchPersonnelRepository: BranchPersonnelRepository,
    private val actorRepository: ActorRepository,
    private val employeeRepository: EmployeeRepository,
    private val objectStorageRepository: ObjectStorageRepository,
    private val branchMapper: BranchMapper,
) : BranchService {

    @Transactional
    @PreAuthorize("hasAuthority('branch_create')")
    override fun createBranch(body: BranchController.BranchPostDTO): BranchController.BranchTableDTO {
        val branch = branchMapper.branchDTOToBranch(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
            profilePicture = body.profilePictureId?.let { objectStorageRepository.getReferenceById(it) }
        }

        val saved = branchRepository.saveAndFlush(branch)
        return branchMapper.branchToDTO(saved)
    }

    @Transactional
    @PreAuthorize("hasAuthority('branch_update')")
    override fun updateBranch(id: UUID, body: BranchController.BranchPutDTO): BranchController.BranchTableDTO {
        val branch = branchRepository.findById(id).orElseThrow {
            NoSuchElementException("Branch not found with ID: $id")
        }. apply {
            branchMapper.branchPutDTOToBranch(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
            profilePicture = body.profilePictureId?.let { objectStorageRepository.getReferenceById(it) }
        }

        branchRepository.saveAndFlush(branch)
        return branchMapper.branchToDTO(branch)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('branch_read')")
    override fun getBranches(pageable: Pageable): Page<BranchController.BranchTableDTO> {
        return branchRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('branch_read')")
    override fun getBranchById(id: UUID): BranchController.BranchTableDTO {
        val branch = branchRepository.findProjectedBy(id).orElseThrow {
            NoSuchElementException("Branch not found with ID: $id")
        }
        return branch
    }

    @Transactional
    @PreAuthorize("hasAuthority('branch_delete')")
    override fun deleteBranch(id: UUID) {
        val branch = branchRepository.findById(id).orElseThrow {
            NoSuchElementException("Branch not found with ID: $id")
        }
        branchRepository.delete(branch)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('branch_read') and hasAuthority('branchPersonnel_read')")
    override fun getBranchEmployees(
        branchId: UUID,
        branchPersonnelStatus: BranchPersonnel.BranchPersonnelStatus?,
        employeeStatus: Employee.EmployeeStatus?,
        pageable: Pageable
    ): Page<BranchController.EmployeeInBranchDTO> {

        // Default values if the parameters are not set:
        val finalBpStatus = branchPersonnelStatus ?: BranchPersonnel.BranchPersonnelStatus.ACTIVE
        val finalEStatus = employeeStatus ?: Employee.EmployeeStatus.IN

        val branchEmployees = branchPersonnelRepository.findAllEmployeesByBranchIdAndStatus(
            branchId = branchId,
            bpStatus = finalBpStatus,
            eStatus = finalEStatus,
            pageable = pageable
        )

        return branchEmployees
    }
}
