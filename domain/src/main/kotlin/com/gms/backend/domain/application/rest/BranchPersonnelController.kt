package com.gms.backend.domain.application.rest

import com.gms.backend.domain.application.mapper.BranchPersonnelMapper
import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.rest.BranchController.BranchPostDTO
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.domain.service.branch.BranchPersonnelService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/branch/personnel")
class BranchPersonnelController(
    private val branchPersonnelService: BranchPersonnelService,
    private val branchPersonnelMapper: BranchPersonnelMapper
) {

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

    data class BranchPersonnelPostDTO(
        val actorId: UUID,
        val branchId: UUID,
        val status: BranchPersonnel.BranchPersonnelStatus = BranchPersonnel.BranchPersonnelStatus.UNDECIDED,
        val createdById: UUID,
    )

    data class BranchPersonnelPutDTO(
        val actorId: UUID,
        val branchId: UUID,
        val status: BranchPersonnel.BranchPersonnelStatus,
        val updatedById: UUID
    )

    @PostMapping
    fun createBranchPersonnel(@RequestBody body: BranchPersonnelPostDTO) =
        branchPersonnelService.createBranchPersonnel(body).toCreatedResponse("Branch Personnel Created")

    @PutMapping("/{id}")
    fun updateBranchPersonnel(@PathVariable id: UUID, @RequestBody body: BranchPersonnelPutDTO) =
        branchPersonnelService.updateBranchPersonnel(id, body).toOkResponse("Branch Personnel Updated")

    @GetMapping
    fun getAllBranchPersonnel() =
        branchPersonnelService.getBranchPersonnel().toOkResponse()

    @GetMapping("/{id}")
    fun getBranchPersonnel(@PathVariable id: UUID) =
        branchPersonnelService.getBranchPersonnelById(id).toOkResponse()

    @DeleteMapping("/{id}")
    fun deleteBranchPersonnel(@PathVariable id: UUID) =
        branchPersonnelService.deleteBranchPersonnel(id).toOkResponse("Branch Personnel Deleted")
}
