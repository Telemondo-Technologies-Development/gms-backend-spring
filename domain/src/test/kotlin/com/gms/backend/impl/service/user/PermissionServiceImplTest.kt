package com.gms.backend.impl.service.permission

import com.gms.backend.domain.impl.domain.service.user.PermissionServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.util.*

class PermissionServiceImplTest
@Autowired constructor(
    private val permissionServiceImpl: PermissionServiceImpl,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadPermissions() {
        // Given
        // When
        val permissions = permissionServiceImpl.getPermissions(Pageable.unpaged())
        // Then
        assertEquals(144, permissions.size)
    }

    @Test
    fun testReadPermission() {
        // Given
        val id =  UUID.fromString("019ba2c0-4a01-79cf-9017-64ca8478a14e")
        // When
        // Then
        assertDoesNotThrow { val permissions = permissionServiceImpl.getPermissionById(id) }
    }
}