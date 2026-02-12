package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.expense.UtilityExpenseService
import io.swagger.v3.oas.annotations.Operation
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
@RequestMapping("/api/utilityExpense")
@Tag(name = "Utility Expense")
class UtilityExpenseController(
    private val utilityExpenseService: UtilityExpenseService
) {
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
    fun getAllUtilityExpense(pageable: Pageable) =
        utilityExpenseService.getUtilityExpense(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    fun getUtilityExpenseById(@PathVariable id: UUID) =
        utilityExpenseService.getUtilityExpenseById(id)

    @PostMapping
    fun createUtilityExpense(@Valid @RequestBody body: UtilityExpenseCreateDTO) =
        utilityExpenseService.createUtilityExpense(body).toOkResponse("Utility Expense Created!")

    @PutMapping("/{id}")
    fun updateUtilityExpense(@PathVariable id: UUID, @Valid @RequestBody body: UtilityExpenseUpdateDTO) =
        utilityExpenseService.updateUtilityExpense(id, body).toOkResponse("Utility Expense Updated!")

    @DeleteMapping("/{id}")
    fun deleteUtilityExpense(@PathVariable id: UUID) =
        utilityExpenseService.deleteUtilityExpense(id).toOkResponse("Utility Expense Deleted!")
}