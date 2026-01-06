package com.gms.backend.domain.application.rest

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.impl.domain.service.user.UserServiceImpl
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserServiceImpl) {

    data class UserTableDTO(
        val id: UUID,
        val email: String,
        val actorId: UUID
    )

    @GetMapping("")
    fun getAllUsers() = userService.getUsers().toOkResponse()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID) =
        userService.getUserById(id).toOkResponse()

    data class UserPostDTO(val email: String, val password: String)

    @PostMapping("")
    fun createUser(@RequestBody body: UserPostDTO) =
        userService.createUser(body).toCreatedResponse()

    data class UserPutDTO(val email: String)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody body: UserPutDTO) =
        userService.updateUser(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID) =
        userService.deleteUser(id).toOkResponse("Deleted Successfully")
}