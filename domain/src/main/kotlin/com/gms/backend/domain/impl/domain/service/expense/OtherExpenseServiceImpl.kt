package com.gms.backend.domain.impl.domain.service.expense

import com.gms.backend.domain.application.mapper.expense.OtherExpenseMapper
import com.gms.backend.domain.application.rest.expense.OtherExpenseController
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.OtherExpenseRepository
import com.gms.backend.domain.domain.repository.expense.OtherExpenseTypeRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.expense.OtherExpenseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class OtherExpenseServiceImpl(
    private val otherExpenseRepository: OtherExpenseRepository,
    private val otherExpenseTypeRepository: OtherExpenseTypeRepository,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository,
    private val otherExpenseMapper: OtherExpenseMapper
) : OtherExpenseService {

    @Transactional
    @PreAuthorize("hasAuthority('member_create')")
    override fun createOtherExpense(body: OtherExpenseController.OtherExpenseCreateDTO): OtherExpenseController.OtherExpenseReadDTO {
        val expense = otherExpenseMapper.toOtherExpense(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            otherExpense = otherExpenseTypeRepository.getReferenceById(body.otherExpenseTypeId)

            val auditActor = actorRepository.getReferenceById(body.actorId)
            createdBy = auditActor
            updatedBy = auditActor

            if (!body.objectIds.isNullOrEmpty()) {
                otherExpensesObjects = body.objectIds.map {
                    objectRepository.getReferenceById(it)
                }.toMutableSet()
            }
        }
        return otherExpenseMapper.toReadDto(otherExpenseRepository.saveAndFlush(expense))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getOtherExpense(pageable: Pageable): Page<OtherExpenseController.OtherExpenseReadDTO> {
        return otherExpenseRepository.findAll(pageable).map(otherExpenseMapper::toReadDto)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getOtherExpenseById(id: UUID): OtherExpenseController.OtherExpenseReadDTO {
        return otherExpenseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Other Expense not found") }
            .let(otherExpenseMapper::toReadDto)
    }

    @Transactional
    @PreAuthorize("hasAuthority('member_update')")
    override fun updateOtherExpense(id: UUID, body: OtherExpenseController.OtherExpenseUpdateDTO): OtherExpenseController.OtherExpenseReadDTO {
        val expense = otherExpenseRepository.findById(id).orElseThrow().apply {
            otherExpenseMapper.updateOtherExpenseFromDto(body, this)
            branch = branchRepository.getReferenceById(body.branchId)
            otherExpense = otherExpenseTypeRepository.getReferenceById(body.otherExpenseTypeId)
            updatedBy = actorRepository.getReferenceById(body.actorId)

            otherExpensesObjects.clear()
            body.objectIds?.let { ids ->
                otherExpensesObjects.addAll(ids.map { objectRepository.getReferenceById(it) })
            }
        }
        return otherExpenseMapper.toReadDto(otherExpenseRepository.saveAndFlush(expense))
    }

    @Transactional
    @PreAuthorize("hasAuthority('member_delete')")
    override fun deleteOtherExpense(id: UUID) {
        if (!otherExpenseRepository.existsById(id)) {
            throw NoSuchElementException("Other Expense not found")
        }
        otherExpenseRepository.deleteById(id)
    }
}