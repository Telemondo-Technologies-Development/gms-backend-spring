package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.expense.UtilityExpenseService
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
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/expense/utility")
@Tag(name = "Utility Expense")
class UtilityExpenseController(
    private val utilityExpenseService: UtilityExpenseService
) {
    @Schema(description = "Request body for creating a new utility expense record (e.g., water, electricity)")
    data class UtilityExpenseCreateDTO(
        @field:NotNull(message = "Meter reading is required")
        val meter: String,

        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Period is required")
        val period: LocalDate,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Utility Type ID is required")
        val utilityTypeId: UUID,

        @field:NotNull(message = "Actor ID (Payer) is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Request body for updating an existing utility expense record")
    data class UtilityExpenseUpdateDTO(
        @field:NotNull(message = "Meter reading is required")
        val meter: String,

        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Period is required")
        val period: LocalDate,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Utility Type ID is required")
        val utilityTypeId: UUID,

        @field:NotNull(message = "Actor ID is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Response data representing utility expense details")
    data class UtilityExpenseReadDTO(
        val id: UUID,
        val meter: String,
        val amount: BigDecimal,
        val period: LocalDate,
        val paidAt: Instant,
        val branchId: UUID?,
        val utilityTypeId: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val objectIds: List<UUID> = emptyList()
    )

    @GetMapping
    @Operation(summary = "Get all utility expenses")
    fun getAllUtilityExpense(pageable: Pageable) =
        utilityExpenseService.getUtilityExpense(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a utility expense by ID")
    fun getUtilityExpenseById(@PathVariable id: UUID) =
        utilityExpenseService.getUtilityExpenseById(id)

    @PostMapping
    @Operation(summary = "Create a utility expense")
    fun createUtilityExpense(@Valid @RequestBody body: UtilityExpenseCreateDTO) =
        utilityExpenseService.createUtilityExpense(body).toOkResponse("Utility Expense Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update a utility expense by ID")
    fun updateUtilityExpense(@PathVariable id: UUID, @Valid @RequestBody body: UtilityExpenseUpdateDTO) =
        utilityExpenseService.updateUtilityExpense(id, body).toOkResponse("Utility Expense Updated!")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a utility expense by ID")
    fun deleteUtilityExpense(@PathVariable id: UUID) =
        utilityExpenseService.deleteUtilityExpense(id).toOkResponse("Utility Expense Deleted!")
}