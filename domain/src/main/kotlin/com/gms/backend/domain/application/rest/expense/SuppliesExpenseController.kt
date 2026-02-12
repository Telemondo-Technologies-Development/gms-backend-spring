package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.expense.SuppliesExpenseService
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
@RequestMapping("/api/suppliesExpense")
@Tag(name = "Supplies Expense")
class SuppliesExpenseController(
    private val suppliesExpenseService: SuppliesExpenseService
) {
    @Schema(description = "Request body for creating a new supplies expense record")
    data class SuppliesExpenseCreateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Supplies Log ID is required")
        val suppliesLogId: UUID,

        @field:NotNull(message = "Actor ID (Payer) is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Request body for updating an existing supplies expense record")
    data class SuppliesExpenseUpdateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Supplies Log ID is required")
        val suppliesLogId: UUID,

        @field:NotNull(message = "Actor ID is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Response data representing supplies expense details")
    data class SuppliesExpenseReadDTO(
        val id: UUID,
        val amount: BigDecimal,
        val paidAt: Instant,
        val branchId: UUID?,
        val suppliesLogId: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val objectIds: List<UUID> = emptyList()
    )

    @GetMapping
    @Operation(summary = "Get all supplies expenses")
    fun getAllSuppliesExpense(pageable: Pageable) =
        suppliesExpenseService.getSuppliesExpense(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a supplies expense by ID")
    fun getSuppliesExpenseById(@PathVariable id: UUID) =
        suppliesExpenseService.getSuppliesExpenseById(id)

    @PostMapping
    @Operation(summary = "Create a supplies expense")
    fun createSuppliesExpense(@Valid @RequestBody body: SuppliesExpenseCreateDTO) =
        suppliesExpenseService.createSuppliesExpense(body).toOkResponse("Supplies Expense Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update a supplies expense by ID")
    fun updateSuppliesExpense(@PathVariable id: UUID, @Valid @RequestBody body: SuppliesExpenseUpdateDTO) =
        suppliesExpenseService.updateSuppliesExpense(id, body).toOkResponse("Supplies Expense Updated!")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a supplies expense by ID")
    fun deleteSuppliesExpense(@PathVariable id: UUID) =
        suppliesExpenseService.deleteSuppliesExpense(id).toOkResponse("Supplies Expense Deleted!")
}