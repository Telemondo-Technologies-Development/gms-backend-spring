package com.gms.backend.impl.service.role

import com.gms.backend.domain.application.rest.user.RoleController
import com.gms.backend.domain.domain.repository.user.RoleRepository
import com.gms.backend.domain.impl.domain.service.user.RoleServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import java.util.*
import kotlin.jvm.optionals.getOrNull

class RoleServiceImplTest
@Autowired constructor(
    private val roleServiceImpl: RoleServiceImpl,
    private val roleRepository: RoleRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadRoles() {
        // Given
        // When
        val roles = roleServiceImpl.getRoles(Pageable.unpaged())
        // Then
        assertEquals(2, roles.size)
    }

    @Test
    fun testCreateRole() {
        // Given
        val createdBy = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val role = RoleController.RolePostDTO("Faculty", "for the faculty", createdBy)
        // When
        val saved = roleServiceImpl.createRole(role)
        // Then
        assertEquals("Faculty", saved.name)
        assertEquals(createdBy, saved.createdById)
        assertThrows<DataIntegrityViolationException>({
            roleServiceImpl.createRole(role)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateRoles() {
        // Given
        val updatedBy = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val role = RoleController.RolePutDTO("SUPERADMIN", "Updated ADMIN to SUPERADMIN", updatedBy)
        val id = UUID.fromString("019ba267-6db6-7541-a171-4f0524691e73")
        // When
        val updated = roleServiceImpl.updateRole(id, role)
        // Then
        assertEquals(updatedBy, role.updatedById)
        assertEquals("SUPERADMIN", updated.name)
    }

    @Test
    fun testDeleteRole() {
        // Given
        val id = UUID.fromString("019c52de-cb08-7221-bbf3-28a31a517376")
        val count = roleRepository.count()
        // When
        val role = roleServiceImpl.deleteRole(id)
        // Then
        assertEquals(count - 1, roleRepository.count())
        assertEquals(null, roleRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            roleServiceImpl.deleteRole(id)
        }
    }
}