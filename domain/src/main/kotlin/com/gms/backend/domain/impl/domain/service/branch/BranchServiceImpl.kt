package com.gms.backend.domain.impl.domain.service.branch

import com.gms.backend.domain.application.mapper.BranchMapper
import com.gms.backend.domain.application.rest.BranchController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.repository.user.EmployeeRepository
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.branch.BranchService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BranchServiceImpl(
    private val branchRepository: BranchRepository,
    private val branchPersonnelRepository: BranchPersonnelRepository,
    private val actorRepository: ActorRepository,
    private val userRepository: UserRepository,
    private val employeeRepository: EmployeeRepository,
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
            branchPersonnelRepository.findAllByBranch_Id(branchId)
        } else {
            branchPersonnelRepository.findAllByBranch_IdAndStatus(branchId, status)
        }
        // if there is no employee under the branch, returns null
        if (rows.isEmpty()) {
            return BranchController.BranchEmployeesDTO(
                branch = branchMapper.branchToSummaryDTO(branch),
                employees = null
            )
        }

        // Collect actors from personnel
        val actorIds = rows.mapNotNull { it.actor?.id }.distinct()

        // load users by actor id
        val users = if (actorIds.isEmpty()) emptyList() else userRepository.findAllByActor_IdIn(actorIds)
        val userByActorId = users.mapNotNull { u -> u.actor?.id?.let { it to u } }.toMap()

        // load employees by user ids
        val userIds = users.mapNotNull { it.id }.distinct()
        val employees = if (userIds.isEmpty()) emptyList() else employeeRepository.findAllByUser_IdIn(userIds)
        val employeeByUserId = employees.mapNotNull { e -> e.user?.id?.let { it to e } }.toMap()

        // Build response
        val result = rows.mapNotNull { bp ->
            val actorId = bp.actor?.id ?: return@mapNotNull null
            val user = userByActorId[actorId]
            val employee = user?.id?.let { employeeByUserId[it] }

            BranchController.EmployeeInBranchDTO(
                actorId = actorId,
                employee = employee?.let(branchMapper::employeeToSummaryDTO)
            )
        }

        return BranchController.BranchEmployeesDTO(
            branch = branchMapper.branchToSummaryDTO(branch),
            employees = result
        )
    }

}
