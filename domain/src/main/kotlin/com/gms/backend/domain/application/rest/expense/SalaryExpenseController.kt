package com.gms.backend.domain.application.rest.expense

import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.expense.SalaryExpense
import com.gms.backend.domain.domain.service.expense.SalaryExpenseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import java.time.Instant

@RestController
@RequestMapping("/api/expense/salary")
@Tag(name = "Salary Expense")
class SalaryExpenseController (
    private val salaryExpenseService: SalaryExpenseService
) {
    @Schema(description = "Request body for creating a new salary expense record")
    data class SalaryExpenseCreateDTO(
        @field:NotNull(message = "Salary type is required")
        val salaryType: SalaryExpense.SalaryType,

        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Period (Year-Month) is required")
        val period: LocalDate,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Actor ID (Employee) is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Request body for updating an existing salary expense record")
    data class SalaryExpenseUpdateDTO(
        @field:NotNull(message = "Salary type is required")
        val salaryType: SalaryExpense.SalaryType,

        @field:Positive(message = "Amount must be greater than zero")
        val amount: BigDecimal,

        @field:NotNull(message = "Period (Year-Month) is required")
        val period: LocalDate,

        @field:NotNull(message = "Payment date is required")
        val paidAt: Instant,

        @field:NotNull(message = "Branch ID is required")
        val branchId: UUID,

        @field:NotNull(message = "Actor ID (Employee) is required")
        val actorId: UUID,

        val objectIds: Set<UUID>? = emptySet()
    )

    @Schema(description = "Response data representing salary expense details")
    data class SalaryExpenseReadDTO(
        val salaryType: SalaryExpense.SalaryType,
        val amount: BigDecimal,
        val period: LocalDate,
        val paidAt: Instant,
        val branchId: UUID?,
        val actorId: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val objectIds: List<UUID> = emptyList()
    )
    @GetMapping
    @Operation(summary = "Get all salary expenses")
    fun getAllSalaryExpense(pageable: Pageable) = salaryExpenseService.getSalaryExpense(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get salary expense by id")
    fun getSalaryExpenseById(@PathVariable id: UUID) = salaryExpenseService.getSalaryExpenseById(id)

    @PostMapping
    @Operation(summary = "Create a new Salary Expense")
    fun createSalaryExpense(@Valid @RequestBody body: SalaryExpenseCreateDTO) =
        salaryExpenseService.createSalaryExpense(body).toOkResponse("Salary Expense Created!")

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Salary Expense by id")
    fun updateSalaryExpense(@PathVariable id: UUID, @Valid @RequestBody body: SalaryExpenseUpdateDTO) =
        salaryExpenseService.updateSalaryExpense(id, body).toOkResponse("Salary Expense Updated!")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a salary Expense by id")
    fun deleteSalaryExpense(@PathVariable id: UUID) =
        salaryExpenseService.deleteSalaryExpense(id).toOkResponse("Salary Expense Deleted!")
}
