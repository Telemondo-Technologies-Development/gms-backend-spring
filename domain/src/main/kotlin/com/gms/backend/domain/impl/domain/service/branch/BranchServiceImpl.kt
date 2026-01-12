package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.branch.BranchMapper
import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.repository.user.EmployeeRepository
import com.gms.backend.domain.domain.service.branch.BranchService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BranchServiceImpl(
    private val branchRepository: BranchRepository,
    private val branchPersonnelRepository: BranchPersonnelRepository,
    private val actorRepository: ActorRepository,
    private val employeeRepository: EmployeeRepository,
    private val objectStorageRepository: ObjectStorageRepository,
    private val branchMapper: BranchMapper,
) : BranchService {

    @Transactional
    override fun createBranch(body: BranchController.BranchPostDTO): BranchController.BranchTableDTO {
        val branch = branchMapper.branchDTOToBranch(body).apply {
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
            profilePicture = body.profilePictureId?.let { objectStorageRepository.getReferenceById(it) }
        }

        val saved = branchRepository.save(branch)
        return branchMapper.branchToDTO(saved)
    }

    @Transactional
    override fun updateBranch(id: UUID, body: BranchController.BranchPutDTO): BranchController.BranchTableDTO {
        val branch = branchRepository.findById(id).orElseThrow {
            NoSuchElementException("Branch not found with ID: $id")
        }. apply {
            branchMapper.branchPutDTOToBranch(body, this)
            this.id = id
            updatedBy = actorRepository.getReferenceById(body.updatedById)
            profilePicture = body.profilePictureId?.let { objectStorageRepository.getReferenceById(it) }
        }

        branchRepository.save(branch)
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

    @Transactional(readOnly = true)
    override fun getBranchEmployees(
        branchId: UUID,
        status: BranchPersonnel.BranchPersonnelStatus?
    ): BranchController.BranchEmployeesDTO {

        val branch = branchRepository.findById(branchId).orElseThrow {
            NoSuchElementException("Branch not found with ID: $branchId")
        }

        val statusToUse = status ?: BranchPersonnel.BranchPersonnelStatus.IN

        // Load branch personnel rows for this branch that match the status filter, if there is no status param provided, then returns all employees
        val rows = if (status == null) {
            branchPersonnelRepository.findAllByBranchId(branchId)
        } else {
            branchPersonnelRepository.findAllByBranchIdAndStatus(branchId, status)
        }
        // if there is no employee under the branch, returns null
        if (rows.isEmpty()) {
            return BranchController.BranchEmployeesDTO(
                branch = branchMapper.branchToSummaryDTO(branch),
                employees = null
            )
        }

        // Collect actors from personnel
        val actorIds = rows.map { it.actorId!! }

        // load employees by actor ids
        val employees = employeeRepository.findAllByActorIdIn(actorIds)
        val employeeByActorId = employees.associateBy { e -> e.actorId }

        // Build response
        val result = rows.map { bp ->
            val actorId = bp.actorId!!
            val employee = employeeByActorId.getValue(actorId)

            BranchController.EmployeeInBranchDTO(
                actorId = actorId,
                employee = employee.let(branchMapper::employeeToSummaryDTO)
            )
        }

        return BranchController.BranchEmployeesDTO(
            branch = branchMapper.branchToSummaryDTO(branch),
            employees = result
        )
    }

}
