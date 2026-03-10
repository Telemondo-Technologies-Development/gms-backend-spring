package com.gms.backend.domain.application.rest.branch

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.domain.service.branch.BranchService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/branch")
@Tag(name = "Branch")
class BranchController (
    private val branchService: BranchService
){

    @Schema(description = "Format for Branch read")
    data class BranchTableDTO(
        val id: UUID,
        val name: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        val status: Branch.BranchStatus,
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant,
        val profilePictureId: UUID?
    )

    @Schema(description = "Format for Branch create")
    data class BranchPostDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        @field:NotBlank(message = "Address must not be empty")
        val address: String,
        @field:Pattern(
            // Validates: Optional negative sign, 1-3 digits, a dot, and 6 to 15 decimal places
            regexp = "^-?\\d{1,3}\\.\\d{6,15}$",
            message = "Longitude must be in decimal format with at least 6 decimal places (e.g., 125.123456)"
        )
        val longitude: String,
        @field:Pattern(
            // Validates: Optional negative sign, 1-3 digits, a dot, and 6 to 15 decimal places
            regexp = "^-?\\d{1,3}\\.\\d{6,15}$",
            message = "Latitude must be in decimal format with at least 6 decimal places (e.g., 125.123456)"
        )
        val latitude: String,
        val status: Branch.BranchStatus = Branch.BranchStatus.UNDECIDED,
        val createdById: UUID,
        val profilePictureId: UUID? = null
    )

    @Schema(description = "Format for Branch update")
    data class BranchPutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        @field:NotBlank(message = "Address must not be empty")
        val address: String,
        @field:Pattern(
            // Validates: Optional negative sign, 1-3 digits, a dot, and 6 to 15 decimal places
            regexp = "^-?\\d{1,3}\\.\\d{6,15}$",
            message = "Longitude must be in decimal format with at least 6 decimal places (e.g., 125.123456)"
        )
        val longitude: String,
        @field:Pattern(
            // Validates: Optional negative sign, 1-3 digits, a dot, and 6 to 15 decimal places
            regexp = "^-?\\d{1,3}\\.\\d{6,15}$",
            message = "Latitude must be in decimal format with at least 6 decimal places (e.g., 125.123456)"
        )
        val latitude: String,
        val status: Branch.BranchStatus,
        val updatedById: UUID,
        val profilePictureId: UUID?
    )

    data class BranchListDTO(
        val id: UUID,
        val name: String
    ): java.io.Serializable {
        companion object {
            private const val serialVersionUID: Long = 1L
        }
    }
    // for branch's list of employees
    data class EmployeeInBranchDTO(
        val actorId: UUID,
        val employeeId: UUID?,
        val employeeSurname: String?,
        val employeeFirstName: String?,
        val employeeMiddleName: String?,
        val employeeSuffix: String?,
        val employeeContactNo: String?,
        val personnelRoleId: UUID?,
        val personnelRoleName: String?,
        val personnelRoleDescription: String?
    )

    // Basic CRUD
    @PostMapping
    @Operation(summary = "Create a new Branch")
    fun createBranch(@Valid @RequestBody body: BranchPostDTO) =
        branchService.createBranch(body).toCreatedResponse("Branch Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Branch by id")
    fun updateBranch(@PathVariable id: UUID, @Valid @RequestBody body: BranchPutDTO) =
        branchService.updateBranch(id, body).toOkResponse("Branch Updated")

    @GetMapping
    @Operation(summary = "Get all Branches")
    fun getAllBranches(pageable: Pageable) =
        branchService.getBranches(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Branch by id")
    fun getBranch(@PathVariable id: UUID) =
        branchService.getBranchById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Branch by id")
    fun deleteBranch(@PathVariable id: UUID) =
        branchService.deleteBranch(id).toOkResponse("Branch Deleted")

    // Extended endpoints
    @GetMapping("/{id}/employee")
    @Operation(summary = "Get all Personnel per Branch by id")
    fun getBranchEmployees(
        @PathVariable id: UUID,
        @RequestParam(required = false) branchPersonnelStatus: BranchPersonnel.BranchPersonnelStatus?,
        @RequestParam(required = false) employeeStatus: Employee.EmployeeStatus?,
        pageable: Pageable
    ) = branchService.getBranchEmployees(id, branchPersonnelStatus,employeeStatus, pageable).toPaginatedResponse()
}
