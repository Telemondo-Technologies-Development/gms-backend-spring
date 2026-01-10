package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.impl.domain.service.user.UserServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/user")
@Tag(name = "User")
class UserController(private val userService: UserServiceImpl) {

    data class UserTableDTO(
        val id: UUID,
        val email: String,
        val actorId: UUID?
    )

    @GetMapping
    @Operation(summary = "Get all Users")
    fun getAllUsers() = userService.getUsers().toOkResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a User by id")
    fun getUser(@PathVariable id: UUID) =
        userService.getUserById(id).toOkResponse()

    data class UserPostDTO(val email: String, val password: String)

    @PostMapping
    @Operation(summary = "Create a new User",
        description = "Creates a new user record in the database")
    fun createUser(@RequestBody body: UserPostDTO) =
        userService.createUser(body).toCreatedResponse()

    data class UserPutDTO(val email: String)

    @PutMapping("/{id}")
    @Operation(summary = "Update a User by id")
    fun updateUser(@PathVariable id: UUID, @RequestBody body: UserPutDTO) =
        userService.updateUser(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a User by id")
    fun deleteUser(@PathVariable id: UUID) =
        userService.deleteUser(id).toOkResponse("Deleted Successfully")
}