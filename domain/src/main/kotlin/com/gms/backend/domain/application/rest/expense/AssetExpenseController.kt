package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.expense.AssetExpenseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/expense/asset")
@Tag(name = "Asset Expense")
class AssetExpenseController(
    private val assetExpenseService: AssetExpenseService
) {
    @Schema(description = "Request body for creating a new asset expense record")
    data class AssetExpenseCreateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Asset ID is required")
        val assetId: UUID,

        @field:NotNull(message = "Actor ID (Payer) is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Request body for updating an existing asset expense record")
    data class AssetExpenseUpdateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Asset ID is required")
        val assetId: UUID,

        @field:NotNull(message = "Actor ID is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Response data representing an asset expense details")
    data class AssetExpenseReadDTO(
        val id: UUID,
        val amount: BigDecimal,
        val paidAt: Instant,
        val branchId: UUID?,
        val assetId: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val objectIds: List<UUID> = emptyList()
    )

    @GetMapping
    @Operation(summary = "Get all asset expenses")
    fun getAllAssetExpense(pageable: Pageable) =
        assetExpenseService.getAssetExpense(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an asset expense by ID")
    fun getAssetExpenseById(@PathVariable id: UUID) =
        assetExpenseService.getAssetExpenseById(id)

    @PostMapping
    @Operation(summary = "Create an asset expense")
    fun createAssetExpense(@Valid @RequestBody body: AssetExpenseCreateDTO) =
        assetExpenseService.createAssetExpense(body).toOkResponse("Asset Expense Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update an asset expense by ID")
    fun updateAssetExpense(@PathVariable id: UUID, @Valid @RequestBody body: AssetExpenseUpdateDTO) =
        assetExpenseService.updateAssetExpense(id, body).toOkResponse("Asset Expense Updated!")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delet an asset expense by ID")
    fun deleteAssetExpense(@PathVariable id: UUID) =
        assetExpenseService.deleteAssetExpense(id).toOkResponse("Asset Expense Deleted!")
}