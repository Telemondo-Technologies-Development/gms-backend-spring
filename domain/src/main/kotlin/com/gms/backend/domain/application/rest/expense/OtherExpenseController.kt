package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.expense.OtherExpenseService
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
import java.util.UUID

@RestController
@RequestMapping("/api/expense/other")
@Tag(name = "Other Expense")
class OtherExpenseController(
    private val otherExpenseService: OtherExpenseService
) {
    @Schema(description = "Request body for creating a new miscellaneous (other) expense record")
    data class OtherExpenseCreateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Other Expense Type ID is required")
        val otherExpenseTypeId: UUID,

        @field:NotNull(message = "Actor ID (Payer) is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Request body for updating an existing miscellaneous (other) expense record")
    data class OtherExpenseUpdateDTO(
        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Other Expense Type ID is required")
        val otherExpenseTypeId: UUID,

        @field:NotNull(message = "Actor ID is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Response data representing miscellaneous (other) expense details")
    data class OtherExpenseReadDTO(
        val id: UUID,
        val amount: BigDecimal,
        val paidAt: Instant,
        val branchId: UUID?,
        val otherExpenseTypeId: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val objectIds: List<UUID> = emptyList()
    )

    @GetMapping
    @Operation(summary = "Get all other expenses")
    fun getAllOtherExpense(pageable: Pageable) =
        otherExpenseService.getOtherExpense(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an other expense by ID")
    fun getOtherExpenseById(@PathVariable id: UUID) =
        otherExpenseService.getOtherExpenseById(id)

    @PostMapping
    @Operation(summary = "Create an other expense")
    fun createOtherExpense(@Valid @RequestBody body: OtherExpenseCreateDTO) =
        otherExpenseService.createOtherExpense(body).toOkResponse("Other Expense Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update an other expense by ID")
    fun updateOtherExpense(@PathVariable id: UUID, @Valid @RequestBody body: OtherExpenseUpdateDTO) =
        otherExpenseService.updateOtherExpense(id, body).toOkResponse("Other Expense Updated!")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an other expense by ID")
    fun deleteOtherExpense(@PathVariable id: UUID) =
        otherExpenseService.deleteOtherExpense(id).toOkResponse("Other Expense Deleted!")
}