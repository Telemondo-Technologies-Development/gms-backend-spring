package com.gms.backend.domain.application.rest

import com.gms.backend.domain.domain.model.user.User
import com.gms.backend.domain.impl.domain.service.user.UserServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getAllUsers(): ResponseEntity<List<UserTableDTO>> {
        val users = userService.getUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: UUID): ResponseEntity<Optional<User>> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    data class UserPostDTO(val email: String, val password: String)

    @PostMapping("")
    fun createUser(@RequestBody body: UserPostDTO): ResponseEntity<String> {
        val something = userService.createUser(body)
        return ResponseEntity.ok("User Successfully Created!")
    }

    data class UserPutDTO(val email: String)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: UUID, @RequestBody body: UserPutDTO): ResponseEntity<String> {
        return try {
            userService.updateUser(id, body)
            ResponseEntity.ok("User updated")
        } catch (e: Exception) {
            // Catch all other exceptions
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating user: ${e.message}")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<String> {
        return try {
            userService.deleteUser(id)
            ResponseEntity.ok("User Deleted")
        } catch (e: Exception) {
            // Catch all other exceptions
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting user: ${e.message}")
        }
    }
}