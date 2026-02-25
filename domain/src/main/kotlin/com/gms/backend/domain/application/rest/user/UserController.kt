package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.user.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/user")
@Tag(name = "User")
class UserController(private val userService: UserService) {

    data class UserTableDTO(
        val id: UUID,
        val email: String,
        val createdAt: Instant,
        val updatedAt: Instant,
        val actorId: UUID?,
    ) {
        var userRoles: List<UserRoleBriefDTO> = emptyList()
    }

    data class UserRoleWithUserId(
        val userId: UUID,
        val id: UUID,
        val name: String
    )

    data class UserRoleBriefDTO(
        val id: UUID,
        val name: String
    )

    data class UserPostDTO(
        @field:Email(message = "Email should be valid")
        val email: String,
        @field:Size(min = 8, max = 64)
        val password: String,
        val roles: List<UUID> = emptyList()
    )

    data class UserPutDTO(
        @field:Email(message = "Email should be valid")
        val email: String,
        val roles: List<UUID> = emptyList()
    )

    @GetMapping
    @Operation(summary = "Get all Users")
    fun getAllUsers(pageable: Pageable) = userService.getUsers(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a User by id")
    fun getUser(@PathVariable id: UUID) =
        userService.getUserById(id).toOkResponse()

    @PostMapping
    @Operation(
        summary = "Create a new User",
        description = "Creates a new user record in the database"
    )
    fun createUser(@Valid @RequestBody body: UserPostDTO) =
        userService.createUser(body).toCreatedResponse()


    @PutMapping("/{id}")
    @Operation(summary = "Update a User by id")
    fun updateUser(@PathVariable id: UUID, @Valid @RequestBody body: UserPutDTO) =
        userService.updateUser(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a User by id")
    fun deleteUser(@PathVariable id: UUID) =
        userService.deleteUser(id).toOkResponse("Deleted Successfully")
}