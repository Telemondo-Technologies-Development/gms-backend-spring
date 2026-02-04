package com.gms.backend.domain.impl.domain.service.expense

import com.gms.backend.domain.application.mapper.expense.AssetExpenseMapper
import com.gms.backend.domain.application.rest.expense.AssetExpenseController
import com.gms.backend.domain.domain.repository.asset.AssetRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.expense.AssetExpenseRepository
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.expense.AssetExpenseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@PreAuthorize("denyAll()")
class AssetExpenseServiceImpl (
    private val assetExpenseRepository: AssetExpenseRepository,
    private val assetExpenseMapper: AssetExpenseMapper,
    private val branchRepository: BranchRepository,
    private val assetRepository: AssetRepository,
    private val actorRepository: ActorRepository,
    private val objectRepository: ObjectStorageRepository
): AssetExpenseService {
   @Transactional
   @PreAuthorize("hasAuthority('member_create')")
   override fun createAssetExpense(body: AssetExpenseController.AssetExpensePostDTO): AssetExpenseController.AssetExpenseResponseDTO {
       val expense = assetExpenseMapper.toAssetExpense(body).apply {
           // Mapping ManyToOne Relationships using References
           branch = branchRepository.getReferenceById(body.branchId)
           asset = assetRepository.getReferenceById(body.assetId)
           // Assuming current user ID is passed in DTO or handled by Security context
           createdBy = actorRepository.getReferenceById(body.createdById)
           updatedBy = actorRepository.getReferenceById(body.createdById)
           // Handling ManyToMany for ObjectStorage
           if (body.objectIds.isNotEmpty()) {
               assetExpensesObjects = body.objectIds.map {
                   objectRepository.getReferenceById(it)
               }.toMutableSet()
           }
       }

       val saved = assetExpenseRepository.saveAndFlush(expense)
       return assetExpenseMapper.toResponseDto(saved)
   }
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getAssetExpenses(pageable: Pageable): Page<AssetExpenseController.AssetExpenseResponseDTO> {
        return assetExpenseRepository.findAll(pageable).map(assetExpenseMapper::toResponseDto)
    }
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('member_read')")
    override fun getAssetExpenseById(id: UUID): AssetExpenseController.AssetExpenseResponseDTO {
        return assetExpenseRepository.findById(id)
            .orElseThrow { NoSuchElementException("Asset Expense not found") }
            .let(assetExpenseMapper::toResponseDto)
    }
    @Transactional
    @PreAuthorize("hasAuthority('member_update')")
    override fun updateAssetExpense(id: UUID, body: AssetExpenseController.AssetExpensePutDTO): AssetExpenseController.AssetExpenseResponseDTO {
        val expense = assetExpenseRepository.findById(id).orElseThrow().apply {
            // Update basic fields via Mapper
            // Note: You'll need to add a @MappingTarget update method in your mapper
            assetExpenseMapper.updateAssetExpenseFromDto(body, this)

            this.id = id
            branch = branchRepository.getReferenceById(body.branchId)
            asset = assetRepository.getReferenceById(body.assetId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)

            // Syncing ManyToMany relationship
            assetExpensesObjects.clear()
            assetExpensesObjects.addAll(body.objectIds.map { objectRepository.getReferenceById(it) })
        }

        assetExpenseRepository.saveAndFlush(expense)
        return assetExpenseMapper.toResponseDto(expense)
    }
    @Transactional
    @PreAuthorize("hasAuthority('member_delete')")
    override fun deleteAssetExpense(id: UUID) {
        if (!assetExpenseRepository.existsById(id)) {
            throw NoSuchElementException("Asset Expense not found")
        }
        assetExpenseRepository.deleteById(id)
    }
}