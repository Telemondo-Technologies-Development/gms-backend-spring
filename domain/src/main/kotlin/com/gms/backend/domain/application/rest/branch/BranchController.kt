package com.gms.backend.domain.application.rest.branch

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.service.branch.BranchService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID

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
        val updatedAt: Instant
    )

    @Schema(description = "Format for Branch create")
    data class BranchPostDTO(
        val name: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        val status: Branch.BranchStatus = Branch.BranchStatus.UNDECIDED,
        val createdById: UUID,
        val profilePictureId: UUID? = null
    )

    @Schema(description = "Format for Branch update")
    data class BranchPutDTO(
        val name: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        val status: Branch.BranchStatus,
        val updatedById: UUID,
        val profilePictureId: UUID?
    )

    // for branch's list of employees
    @Schema(description = "Format for Branch read summary")
    data class BranchSummaryDTO(
        val id: UUID,
        val name: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        val status: Branch.BranchStatus
    )

    data class EmployeeSummaryDTO(
        val id: UUID,
        val surname: String?,
        val firstName: String?,
        val middleName: String?,
        val suffix: String?
    )

    data class EmployeeInBranchDTO(
        val actorId: UUID,
        val employee: EmployeeSummaryDTO?
    )

    @Schema(description = "Format for Branch with Personnel read")
    data class BranchEmployeesDTO(
        val branch: BranchSummaryDTO,
        val employees: List<EmployeeInBranchDTO>?
    )

    @PostMapping
    @Operation(summary = "Create a new Branch")
    fun createBranch(@RequestBody body: BranchPostDTO) =
        branchService.createBranch(body).toCreatedResponse("Branch Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Branch by id")
    fun updateBranch(@PathVariable id: UUID, @RequestBody body: BranchPutDTO) =
        branchService.updateBranch(id, body).toOkResponse("Branch Updated")

    @GetMapping
    @Operation(summary = "Get all Branches")
    fun getAllBranches() =
        branchService.getBranches().toOkResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Branch by id")
    fun getBranch(@PathVariable id: UUID) =
        branchService.getBranchById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Branch by id")
    fun deleteBranch(@PathVariable id: UUID) =
        branchService.deleteBranch(id).toOkResponse("Branch Deleted")

    @GetMapping("/{id}/employees")
    @Operation(summary = "Get all Personnel per Branch by id")
    fun getBranchEmployees(
        @PathVariable id: UUID,
        @RequestParam(required = false) status: BranchPersonnel.BranchPersonnelStatus?
    ) = branchService.getBranchEmployees(id, status).toOkResponse()
}
