package com.gms.backend.impl.service.user

import com.gms.backend.domain.application.rest.user.UserController
import com.gms.backend.domain.impl.domain.service.user.UserServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class UserServiceImplTest
@Autowired constructor(private val userServiceImpl: UserServiceImpl, private val nc: Connection) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadUsers() {
        val users = userServiceImpl.getUsers()
        assertEquals(5, users.size)
    }

    @Test
    fun testCreateUser() {
        val user = UserController.UserPostDTO("test", "test")
        val saved = userServiceImpl.createUser(user)
        assertEquals("test", saved.email)
    }

    @Test
    fun testUpdateUsers() {
        val user = UserController.UserPutDTO("new")
        val updated = userServiceImpl.updateUser(UUID.fromString("019ba28a-c6e5-727a-88fd-1b640603d4e6"), user)
        assertNotNull(updated.actorId)
        assertEquals("new", updated.email)
    }

    @Test
    fun testDeleteUser() {
        val user = userServiceImpl.deleteUser(UUID.fromString("019ba28a-c6e5-727a-88fd-1b640603d4e6"))
    }
}