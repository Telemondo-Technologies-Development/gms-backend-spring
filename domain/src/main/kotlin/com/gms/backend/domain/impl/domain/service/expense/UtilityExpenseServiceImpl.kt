package com.gms.backend.domain.impl.domain.service.expense

import com.gms.backend.domain.application.mapper.expense.UtilityExpenseMapper
import com.gms.backend.domain.application.rest.expense.UtilityExpenseController
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.UtilityExpenseRepository
import com.gms.backend.domain.domain.repository.expense.UtilityTypeRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.expense.UtilityExpenseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class UtilityExpenseServiceImpl(
    private val utilityExpenseRepository: UtilityExpenseRepository,
    private val utilityTypeRepository: UtilityTypeRepository,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository,
    private val utilityExpenseMapper: UtilityExpenseMapper
) : UtilityExpenseService {

    @Transactional
    @PreAuthorize("hasAuthority('utilityExpense_create')")
    override fun createUtilityExpense(body: UtilityExpenseController.UtilityExpenseCreateDTO): UtilityExpenseController.UtilityExpenseReadDTO {
        val expense = utilityExpenseMapper.toUtilityExpense(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            utilityType = utilityTypeRepository.getReferenceById(body.utilityTypeId)

            val auditActor = actorRepository.getReferenceById(body.actorId)
            createdBy = auditActor
            updatedBy = auditActor

            if (!body.objectIds.isNullOrEmpty()) {
                utilityExpensesObjects = body.objectIds.map {
                    objectRepository.getReferenceById(it)
                }.toMutableSet()
            }
        }
        return utilityExpenseMapper.toReadDto(utilityExpenseRepository.saveAndFlush(expense))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('utilityExpense_read')")
    override fun getUtilityExpense(pageable: Pageable): Page<UtilityExpenseController.UtilityExpenseReadDTO> {
        return utilityExpenseRepository.findAll(pageable).map(utilityExpenseMapper::toReadDto)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('utilityExpense_read')")
    override fun getUtilityExpenseById(id: UUID): UtilityExpenseController.UtilityExpenseReadDTO {
        return utilityExpenseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Utility Expense not found") }
            .let(utilityExpenseMapper::toReadDto)
    }

    @Transactional
    @PreAuthorize("hasAuthority('utilityExpense_update')")
    override fun updateUtilityExpense(id: UUID, body: UtilityExpenseController.UtilityExpenseUpdateDTO): UtilityExpenseController.UtilityExpenseReadDTO {
        val expense = utilityExpenseRepository.findById(id).orElseThrow().apply {
            utilityExpenseMapper.updateUtilityExpenseFromDto(body, this)
            branch = branchRepository.getReferenceById(body.branchId)
            utilityType = utilityTypeRepository.getReferenceById(body.utilityTypeId)
            updatedBy = actorRepository.getReferenceById(body.actorId)

            utilityExpensesObjects.clear()
            body.objectIds?.let { ids ->
                utilityExpensesObjects.addAll(ids.map { objectRepository.getReferenceById(it) })
            }
        }
        return utilityExpenseMapper.toReadDto(utilityExpenseRepository.saveAndFlush(expense))
    }

    @Transactional
    @PreAuthorize("hasAuthority('utilityExpense_delete')")
    override fun deleteUtilityExpense(id: UUID) {
        if (!utilityExpenseRepository.existsById(id)) {
            throw NoSuchElementException("Utility Expense not found")
        }
        utilityExpenseRepository.deleteById(id)
    }
}