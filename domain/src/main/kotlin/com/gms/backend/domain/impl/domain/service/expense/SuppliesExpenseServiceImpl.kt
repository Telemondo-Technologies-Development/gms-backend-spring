package com.gms.backend.domain.impl.domain.service.expense

import com.gms.backend.domain.application.mapper.expense.SuppliesExpenseMapper
import com.gms.backend.domain.application.rest.expense.SuppliesExpenseController
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.SuppliesExpenseRepository
import com.gms.backend.domain.domain.repository.asset.SuppliesLogRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.expense.SuppliesExpenseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class SuppliesExpenseServiceImpl(
    private val suppliesExpenseRepository: SuppliesExpenseRepository,
    private val branchRepository: BranchRepository,
    private val suppliesLogRepository: SuppliesLogRepository,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository,
    private val suppliesExpenseMapper: SuppliesExpenseMapper
) : SuppliesExpenseService {

    @Transactional
    @PreAuthorize("hasAuthority('member_create')")
    override fun createSuppliesExpense(body: SuppliesExpenseController.SuppliesExpenseCreateDTO): SuppliesExpenseController.SuppliesExpenseReadDTO {
        val expense = suppliesExpenseMapper.toSuppliesExpense(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            suppliesLog = suppliesLogRepository.getReferenceById(body.suppliesLogId)

            val auditActor = actorRepository.getReferenceById(body.actorId)
            createdBy = auditActor
            updatedBy = auditActor

            if (!body.objectIds.isNullOrEmpty()) {
                suppliesExpensesObjects = body.objectIds.map {
                    objectRepository.getReferenceById(it)
                }.toMutableSet()
            }
        }
        return suppliesExpenseMapper.toReadDto(suppliesExpenseRepository.saveAndFlush(expense))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getSuppliesExpense(pageable: Pageable): Page<SuppliesExpenseController.SuppliesExpenseReadDTO> {
        return suppliesExpenseRepository.findAll(pageable).map(suppliesExpenseMapper::toReadDto)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getSuppliesExpenseById(id: UUID): SuppliesExpenseController.SuppliesExpenseReadDTO {
        return suppliesExpenseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Supplies Expense not found") }
            .let(suppliesExpenseMapper::toReadDto)
    }

    @Transactional
    @PreAuthorize("hasAuthority('member_update')")
    override fun updateSuppliesExpense(id: UUID, body: SuppliesExpenseController.SuppliesExpenseUpdateDTO): SuppliesExpenseController.SuppliesExpenseReadDTO {
        val expense = suppliesExpenseRepository.findById(id).orElseThrow().apply {
            suppliesExpenseMapper.updateSuppliesExpenseFromDto(body, this)
            branch = branchRepository.getReferenceById(body.branchId)
            suppliesLog = suppliesLogRepository.getReferenceById(body.suppliesLogId)
            updatedBy = actorRepository.getReferenceById(body.actorId)

            suppliesExpensesObjects.clear()
            body.objectIds?.let { ids ->
                suppliesExpensesObjects.addAll(ids.map { objectRepository.getReferenceById(it) })
            }
        }
        return suppliesExpenseMapper.toReadDto(suppliesExpenseRepository.saveAndFlush(expense))
    }

    @Transactional
    @PreAuthorize("hasAuthority('member_delete')")
    override fun deleteSuppliesExpense(id: UUID) {
        if (!suppliesExpenseRepository.existsById(id)) {
            throw NoSuchElementException("Supplies Expense not found")
        }
        suppliesExpenseRepository.deleteById(id)
    }
}