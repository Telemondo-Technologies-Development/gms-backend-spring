package com.gms.backend.domain.impl.domain.service.expense

import com.gms.backend.domain.application.mapper.expense.AssetMaintenanceExpenseMapper
import com.gms.backend.domain.application.rest.expense.AssetMaintenanceExpenseController
import com.gms.backend.domain.domain.repository.asset.AssetMaintenanceRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.AssetMaintenanceExpenseRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.expense.AssetMaintenanceExpenseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class AssetMaintenanceExpenseServiceImpl(
    private val assetMaintenanceExpenseRepository: AssetMaintenanceExpenseRepository,
    private val assetMaintenanceRepository: AssetMaintenanceRepository,
    private val branchRepository: BranchRepository,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository,
    private val assetMaintenanceExpenseMapper: AssetMaintenanceExpenseMapper
) : AssetMaintenanceExpenseService {

    @Transactional
    @PreAuthorize("hasAuthority('assetMaintenanceExpense_create')")
    override fun createAssetMaintenanceExpense(body: AssetMaintenanceExpenseController.AssetMaintenanceExpenseCreateDTO): AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO {
        val expense = assetMaintenanceExpenseMapper.toAssetMaintenanceExpense(body).apply {
            branch = branchRepository.getReferenceById(body.branchId)
            assetMaintenance = assetMaintenanceRepository.getReferenceById(body.assetMaintenanceId)

            val auditActor = actorRepository.getReferenceById(body.actorId)
            createdBy = auditActor
            updatedBy = auditActor

            if (!body.objectIds.isNullOrEmpty()) {
                assetMaintenanceExpensesObjects = body.objectIds.map {
                    objectRepository.getReferenceById(it)
                }.toMutableSet()
            }
        }
        return assetMaintenanceExpenseMapper.toReadDto(assetMaintenanceExpenseRepository.saveAndFlush(expense))
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetMaintenanceExpense_read')")
    override fun getAssetMaintenanceExpense(pageable: Pageable): Page<AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO> {
        return assetMaintenanceExpenseRepository.findAll(pageable).map(assetMaintenanceExpenseMapper::toReadDto)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('assetMaintenanceExpense_read')")
    override fun getAssetMaintenanceExpenseById(id: UUID): AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO {
        return assetMaintenanceExpenseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Asset Maintenance Expense not found") }
            .let(assetMaintenanceExpenseMapper::toReadDto)
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetMaintenanceExpense_update')")
    override fun updateAssetMaintenanceExpense(id: UUID, body: AssetMaintenanceExpenseController.AssetMaintenanceExpenseUpdateDTO): AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO {
        val expense = assetMaintenanceExpenseRepository.findById(id).orElseThrow().apply {
            assetMaintenanceExpenseMapper.updateFromDto(body, this)
            branch = branchRepository.getReferenceById(body.branchId)
            assetMaintenance = assetMaintenanceRepository.getReferenceById(body.assetMaintenanceId)
            updatedBy = actorRepository.getReferenceById(body.actorId)

            assetMaintenanceExpensesObjects.clear()
            body.objectIds?.let { ids ->
                assetMaintenanceExpensesObjects.addAll(ids.map { objectRepository.getReferenceById(it) })
            }
        }
        return assetMaintenanceExpenseMapper.toReadDto(assetMaintenanceExpenseRepository.saveAndFlush(expense))
    }

    @Transactional
    @PreAuthorize("hasAuthority('assetMaintenanceExpense_delete')")
    override fun deleteAssetMaintenanceExpense(id: UUID) {
        if (!assetMaintenanceExpenseRepository.existsById(id)) {
            throw NoSuchElementException("Asset Maintenance Expense not found")
        }
        assetMaintenanceExpenseRepository.deleteById(id)
    }
}