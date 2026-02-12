package com.gms.backend.domain.impl.domain.service.expense

import com.gms.backend.domain.application.mapper.expense.SalaryExpenseMapper
import com.gms.backend.domain.application.rest.expense.SalaryExpenseController
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.SalaryExpenseRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.expense.SalaryExpenseService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page
import java.util.UUID

@Service
@PreAuthorize("denyAll()")
class SalaryExpenseServiceImpl (
    private val salaryExpenseRepository: SalaryExpenseRepository,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository,
    private val salaryExpenseMapper: SalaryExpenseMapper
): SalaryExpenseService {
    @Transactional
    @PreAuthorize("hasAuthority('salaryExpense_create')")
    override fun createSalaryExpense(body: SalaryExpenseController.SalaryExpenseCreateDTO): SalaryExpenseController.SalaryExpenseReadDTO {
        val expense = salaryExpenseMapper.toSalaryExpense(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            actor = actorRepository.getReferenceById(body.actorId)
            createdBy = actorRepository.getReferenceById(body.actorId)
            updatedBy = actorRepository.getReferenceById(body.actorId)

            if (!body.objectIds.isNullOrEmpty()) {
                salaryExpensesObjects = body.objectIds.map {
                    objectRepository.getReferenceById(it)
                }.toMutableSet()
            }
        }
        val saved = salaryExpenseRepository.saveAndFlush(expense)
        return salaryExpenseMapper.toReadDto(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('salaryExpense_read')")
    override fun getSalaryExpense(pageable: Pageable): Page<SalaryExpenseController.SalaryExpenseReadDTO> {
        return salaryExpenseRepository.findAll(pageable).map(salaryExpenseMapper::toReadDto)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('salaryExpense_read')")
    override fun getSalaryExpenseById(id: UUID): SalaryExpenseController.SalaryExpenseReadDTO {
        return salaryExpenseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Asset Expense not found") }
            .let(salaryExpenseMapper::toReadDto)
    }

    @Transactional
    @PreAuthorize("hasAuthority('salaryExpense_update')")
    override fun updateSalaryExpense(
        id: UUID,
        body: SalaryExpenseController.SalaryExpenseUpdateDTO
    ): SalaryExpenseController.SalaryExpenseReadDTO {
        val expense = salaryExpenseRepository.findById(id).orElseThrow().apply {
            salaryExpenseMapper.updateSalaryExpenseFromDto(body, this)
            branch = branchRepository.getReferenceById(body.branchId)
            actor = actorRepository.getReferenceById(body.actorId)
            updatedBy = actorRepository.getReferenceById(body.actorId)
            salaryExpensesObjects.clear()
            body.objectIds?.let { ids ->
                salaryExpensesObjects.addAll(ids.map { objectRepository.getReferenceById(it) })
            }
        }
        return salaryExpenseMapper.toReadDto(salaryExpenseRepository.saveAndFlush(expense))
    }

    @Transactional
    @PreAuthorize("hasAuthority('salaryExpense_delete')")
    override fun deleteSalaryExpense(id: UUID) {
        if (!salaryExpenseRepository.existsById(id)) {
            throw NoSuchElementException("Asset Expense not found")
        }
        salaryExpenseRepository.deleteById(id)
    }

}