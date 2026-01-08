package com.gms.backend.domain.application.rest

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.service.branch.BranchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/branch")
class BranchController (
    private val branchService: BranchService
){

    data class BranchTableDTO(
        val id: UUID,
        val name: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        val status: Branch.BranchStatus,
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant?,
        val updatedAt: Instant?
    )

    data class BranchPostDTO(
        val name: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        val status: Branch.BranchStatus = Branch.BranchStatus.UNDECIDED,
        val createdById: UUID,
        val profilePictureObjectId: UUID? = null
    )

    data class BranchPutDTO(
        val name: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        val status: Branch.BranchStatus,
        val updatedById: UUID,
        val profilePictureObjectId: UUID?
    )

    @PostMapping
    fun createBranch(@RequestBody body: BranchPostDTO) =
        branchService.createBranch(body).toCreatedResponse("Branch Created")

    @PutMapping("/{id}")
    fun updateBranch(@PathVariable id: UUID, @RequestBody body: BranchPutDTO) =
        branchService.updateBranch(id, body).toOkResponse("Branch Updated")

    @GetMapping
    fun getAllBranches() =
        branchService.getBranches().toOkResponse()

    @GetMapping("/{id}")
    fun getBranch(@PathVariable id: UUID) =
        branchService.getBranchById(id).toOkResponse()

    @DeleteMapping("/{id}")
    fun deleteBranch(@PathVariable id: UUID) =
        branchService.deleteBranch(id).toOkResponse("Branch Deleted")
}
