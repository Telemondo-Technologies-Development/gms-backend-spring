package com.gms.backend.domain.application.rest.branch

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.service.branch.BranchPersonnelService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/branch/personnel")
@Tag(name = "Branch Personnel")
class BranchPersonnelController(
    private val branchPersonnelService: BranchPersonnelService
) {

    @Schema(description = "Format for Branch Personnel read")
    data class BranchPersonnelTableDTO(
        val id: UUID,
        val actorId: UUID,
        val branchId: UUID,
        val status: BranchPersonnel.BranchPersonnelStatus,
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    @Schema(description = "Format for Branch Personnel create")
    data class BranchPersonnelPostDTO(
        val actorId: UUID,
        val branchId: UUID,
        val status: BranchPersonnel.BranchPersonnelStatus = BranchPersonnel.BranchPersonnelStatus.UNDECIDED,
        val createdById: UUID,
    )

    @Schema(description = "Format for Branch Personnel update")
    data class BranchPersonnelPutDTO(
        val actorId: UUID,
        val branchId: UUID,
        val status: BranchPersonnel.BranchPersonnelStatus,
        val updatedById: UUID
    )

    @PostMapping
    @Operation(summary = "Create a new Branch Personnel")
    fun createBranchPersonnel(@RequestBody body: BranchPersonnelPostDTO) =
        branchPersonnelService.createBranchPersonnel(body).toCreatedResponse("Branch Personnel Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Branch Personnel by id")
    fun updateBranchPersonnel(@PathVariable id: UUID, @RequestBody body: BranchPersonnelPutDTO) =
        branchPersonnelService.updateBranchPersonnel(id, body).toOkResponse("Branch Personnel Updated")

    @GetMapping
    @Operation(summary = "Get all Branch Personnel")
    fun getAllBranchPersonnel(pageable: Pageable) =
        branchPersonnelService.getBranchPersonnel(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Branch Personnel by id")
    fun getBranchPersonnel(@PathVariable id: UUID) =
        branchPersonnelService.getBranchPersonnelById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Branch Personnel by id")
    fun deleteBranchPersonnel(@PathVariable id: UUID) =
        branchPersonnelService.deleteBranchPersonnel(id).toOkResponse("Branch Personnel Deleted")
}
