package com.gms.backend.domain.impl.domain.service.expense

import com.gms.backend.domain.application.mapper.expense.AssetExpenseMapper
import com.gms.backend.domain.application.rest.expense.AssetExpenseController
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.AssetExpenseRepository
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.expense.AssetExpenseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class AssetExpenseServiceImpl(
    private val assetExpenseRepository: AssetExpenseRepository,
    private val assetRepository: AssetRepository,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository,
    private val assetExpenseMapper: AssetExpenseMapper
) : AssetExpenseService {

    @Transactional
    @PreAuthorize("hasAuthority('assetExpense_create')")
    override fun createAssetExpense(body: AssetExpenseController.AssetExpenseCreateDTO): AssetExpenseController.AssetExpenseReadDTO {
        val expense = assetExpenseMapper.toAssetExpense(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            asset = assetRepository.getReferenceById(body.assetId)

            val auditActor = actorRepository.getReferenceById(body.actorId)
            createdBy = auditActor
            updatedBy = auditActor

            if (!body.objectIds.isNullOrEmpty()) {
                assetExpensesObjects = body.objectIds.map {
                    objectRepository.getReferenceById(it)
                }.toMutableSet()
            }
        }
        return assetExpenseMapper.toReadDto(assetExpenseRepository.saveAndFlush(expense))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetExpense_read')")
    override fun getAssetExpense(pageable: Pageable): Page<AssetExpenseController.AssetExpenseReadDTO> {
        return assetExpenseRepository.findAll(pageable).map(assetExpenseMapper::toReadDto)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetExpense_read')")
    override fun getAssetExpenseById(id: UUID): AssetExpenseController.AssetExpenseReadDTO {
        return assetExpenseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Asset Expense not found") }
            .let(assetExpenseMapper::toReadDto)
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetExpense_update')")
    override fun updateAssetExpense(id: UUID, body: AssetExpenseController.AssetExpenseUpdateDTO): AssetExpenseController.AssetExpenseReadDTO {
        val expense = assetExpenseRepository.findById(id).orElseThrow().apply {
            assetExpenseMapper.updateFromDto(body, this)
            branch = branchRepository.getReferenceById(body.branchId)
            asset = assetRepository.getReferenceById(body.assetId)
            updatedBy = actorRepository.getReferenceById(body.actorId)

            assetExpensesObjects.clear()
            body.objectIds?.let { ids ->
                assetExpensesObjects.addAll(ids.map { objectRepository.getReferenceById(it) })
            }
        }
        return assetExpenseMapper.toReadDto(assetExpenseRepository.saveAndFlush(expense))
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetExpense_delete')")
    override fun deleteAssetExpense(id: UUID) {
        if (!assetExpenseRepository.existsById(id)) {
            throw NoSuchElementException("Asset Expense not found")
        }
        assetExpenseRepository.deleteById(id)
    }
}