package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.application.rest.storage.ObjectStorageController
import com.gms.backend.domain.domain.service.expense.AssetExpenseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID
import java.time.Instant

@RestController
@RequestMapping("/api/assetExpense")
@Tag(name = "Asset Expense")
class AssetExpenseController(
    private val assetExpenseService: AssetExpenseService
) {

    data class AssetExpensePostDTO(
        @field:NotNull(message = "Amount is required")
        @field:DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        val amount: BigDecimal,
        @field:NotNull(message = "Payment date is required")
        @field:PastOrPresent(message = "Payment date cannot be in the future")
        val paidAt: Instant,
        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,
        @field:NotNull(message = "Asset ID is required")
        val assetId: UUID,
        val createdById: UUID,
        val updatedById: UUID,
        // List of IDs for the ManyToMany attachments
        val objectIds: Set<UUID> = emptySet()
    )
    data class AssetExpensePutDTO(
        @field:NotNull(message = "Amount is required")
        @field:DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        val amount: BigDecimal,
        @field:NotNull(message = "Payment date is required")
        @field:PastOrPresent(message = "Payment date cannot be in the future")
        val paidAt: Instant,
        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,
        @field:NotNull(message = "Asset ID is required")
        val assetId: UUID,
        val updatedById: UUID,
        // List of IDs for the ManyToMany attachments
        val objectIds: Set<UUID> = emptySet()
    )
    data class AssetExpenseResponseDTO(
        val id: UUID,
        val amount: BigDecimal,
        val paidAt: Instant,
        val branchId: UUID?,
        val assetId: UUID,
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val objects: List<ObjectStorageController.ObjectStorageDTO> = emptyList()
    )
    // for summary in case they use it for dashboard showing expenses
    data class AssetExpenseSummaryDTO(
        val id: UUID,
        val amount: BigDecimal,
        val paidAt: Instant,
        val assetName: String // Useful so the UI doesn't have to do another lookup
    )
    @GetMapping
    @Operation(summary = "Get all asset expense details")
    fun getAllAssetExpense(pageable: Pageable) = assetExpenseService.getAssetExpenses(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get Asset Expense by id")
    fun getAssetExpenseById(@PathVariable id: UUID) = assetExpenseService.getAssetExpenseById(id)

    @PostMapping
    @Operation(summary = "Create a new Asset Expense")
    fun createAssetExpense(@Valid @RequestBody body: AssetExpensePostDTO) =
        assetExpenseService.createAssetExpense(body).toCreatedResponse("Asset Expense Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Asset Expense by id")
    fun updateAssetExpense(@PathVariable id: UUID, @Valid @RequestBody body: AssetExpensePutDTO) =
        assetExpenseService.updateAssetExpense(id, body).toOkResponse("Asset Expense Updated!")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing Asset Expense by id")
    fun deleteAssetExpense(@PathVariable id: UUID) =
        assetExpenseService.deleteAssetExpense(id).toOkResponse("Asset Expense Deleted!")
}