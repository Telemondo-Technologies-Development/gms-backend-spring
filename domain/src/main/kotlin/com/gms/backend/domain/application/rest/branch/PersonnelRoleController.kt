package com.gms.backend.domain.application.rest.branch

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.branch.PersonnelRoleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/branch/personnel/role")
@Tag(name = "Branch Personnel Roles")
class PersonnelRoleController(private val personnelRoleService: PersonnelRoleService) {

    @Schema(description = "Format for Personnel Role read")
    data class PersonnelRoleTableDTO(
        val id: UUID,
        val name: String,
        val description: String?,
        val createdById: UUID?,
        val updatedById: UUID?
    )

    @Schema(description = "Format for Personnel Role create")
    data class PersonnelRolePostDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        @field:Size(min = 1, message = "Description cannot be blank if provided")
        val description: String?,
        val createdById: UUID
    )

    @Schema(description = "Format for Personnel Role update")
    data class PersonnelRolePutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        @field:Size(min = 1, message = "Description cannot be blank if provided")
        val description: String?,
        val updatedById: UUID
    )

    @GetMapping
    @Operation(summary = "Get all Personnel Roles")
    fun getAllPersonnelRoles(pageable: Pageable) =
        personnelRoleService.getPersonnelRoles(pageable).toPaginatedResponse()


    @GetMapping("/{id}")
    @Operation(summary = "Get a Personnel Role by id")
    fun getPersonnelRole(@PathVariable id: UUID) =
        personnelRoleService.getPersonnelRoleById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Personnel Role")
    fun createPersonnelRole(@Valid @RequestBody body: PersonnelRolePostDTO) =
        personnelRoleService.createPersonnelRole(body).toCreatedResponse()

    @Operation(summary = "Update a Personnel Role by id")
    @PutMapping("/{id}")
    fun updatePersonnelRole(@PathVariable id: UUID, @Valid @RequestBody body: PersonnelRolePutDTO) =
        personnelRoleService.updatePersonnelRole(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Personnel Role by id")
    fun deletePersonnelRole(@PathVariable id: UUID) =
        personnelRoleService.deletePersonnelRole(id).toOkResponse("Deleted Successfully")

}