package com.gms.backend.impl.service.user

import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.impl.domain.service.user.UserServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.jvm.optionals.getOrNull

class UserServiceImplTest
@Autowired constructor(
    private val userServiceImpl: UserServiceImpl,
    private val userRepository: UserRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadUsers() {
        // Given
        // When
        val users = userServiceImpl.getUsers(Pageable.unpaged())
        // Then
        assertEquals(5, users.size)
    }

    @Test
    fun testCreateUser() {
        // Given
        val user = UserController.UserPostDTO("test@email.com", "password")
        // When
        val saved = userServiceImpl.createUser(user)
        // Then
        assertEquals("test@email.com", saved.username)
        assertThrows<DataIntegrityViolationException>({
            userServiceImpl.createUser(user)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateUsers() {
        // Given
        val user = UserController.UserPutDTO("new@email.com")
        val id = UUID.fromString("019ba28a-c6e5-727a-88fd-1b640603d4e6")
        // When
        val updated = userServiceImpl.updateUser(id, user)
        // Then
        assertNotNull(updated.actorId)
        assertEquals("new@email.com", updated.username)
    }

    @Test
    fun testDeleteUser() {
        // Then
        val id = UUID.fromString("019ba28a-c6e5-727a-88fd-1b640603d4e6")
        val count = userRepository.count()
        // When
        val user = userServiceImpl.deleteUser(id)
        // Then
        assertEquals(count - 1, userRepository.count())
        assertEquals(null, userRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            userServiceImpl.deleteUser(id)
        }
    }
}