package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.expense.AssetMaintenanceExpenseService
import io.swagger.v3.oas.annotations.Operation
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
@RequestMapping("/api/assetMaintenanceExpense")
@Tag(name = "Asset Maintenance Expense")
class AssetMaintenanceExpenseController(
    private val assetMaintenanceExpenseService: AssetMaintenanceExpenseService
) {
    data class AssetMaintenanceExpenseCreateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Asset Maintenance ID is required")
        val assetMaintenanceId: UUID,

        @field:NotNull(message = "Actor ID (Payer) is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    data class AssetMaintenanceExpenseUpdateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Asset Maintenance ID is required")
        val assetMaintenanceId: UUID,

        @field:NotNull(message = "Actor ID is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    data class AssetMaintenanceExpenseReadDTO(
        val id: UUID,
        val amount: BigDecimal,
        val paidAt: Instant,
        val branchId: UUID?,
        val assetMaintenanceId: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val objectIds: List<UUID> = emptyList()
    )

    @GetMapping
    fun getAllAssetMaintenanceExpense(pageable: Pageable) =
        assetMaintenanceExpenseService.getAssetMaintenanceExpense(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    fun getAssetMaintenanceExpenseById(@PathVariable id: UUID) =
        assetMaintenanceExpenseService.getAssetMaintenanceExpenseById(id)

    @PostMapping
    fun createAssetMaintenanceExpense(@Valid @RequestBody body: AssetMaintenanceExpenseCreateDTO) =
        assetMaintenanceExpenseService.createAssetMaintenanceExpense(body).toOkResponse("Maintenance Expense Created!")

    @PutMapping("/{id}")
    fun updateAssetMaintenanceExpense(@PathVariable id: UUID, @Valid @RequestBody body: AssetMaintenanceExpenseUpdateDTO) =
        assetMaintenanceExpenseService.updateAssetMaintenanceExpense(id, body).toOkResponse("Maintenance Expense Updated!")

    @DeleteMapping("/{id}")
    fun deleteAssetMaintenanceExpense(@PathVariable id: UUID) =
        assetMaintenanceExpenseService.deleteAssetMaintenanceExpense(id).toOkResponse("Maintenance Expense Deleted!")
}