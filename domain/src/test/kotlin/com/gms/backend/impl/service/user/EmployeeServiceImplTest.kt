package com.gms.backend.impl.service.employee

import com.gms.backend.domain.application.rest.user.EmployeeController
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.domain.repository.user.EmployeeRepository
import com.gms.backend.domain.impl.domain.service.user.EmployeeServiceImpl
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

class EmployeeServiceImplTest
@Autowired constructor(
    private val employeeServiceImpl: EmployeeServiceImpl,
    private val employeeRepository: EmployeeRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadEmployees() {
        // Given
        // When
        val employees = employeeServiceImpl.getEmployees(Pageable.unpaged())
        // Then
        assertEquals(3, employees.size)
    }

    @Test
    fun testCreateEmployee() {
        // Given
        val userId = UUID.fromString("019ba28a-c6e5-727a-88fd-1b640603d4e6")
        val employee = EmployeeController.EmployeePostDTO(
            userId = userId,
            surname = "Ford",
            firstName = "Michaela",
            contactNo = "12345678901",
            status = Employee.EmployeeStatus.IN,
            middleName = null,
            suffix = null,
            profilePictureId = null
        )
        // When
        val saved = employeeServiceImpl.createEmployee(employee)
        // Then
        assertEquals("Ford", saved.surname)
        assertEquals("12345678901", saved.contactNo)
        assertEquals(Employee.EmployeeStatus.IN, saved.status)
        assertThrows<DataIntegrityViolationException>({
            employeeServiceImpl.createEmployee(employee)
            entityManager.flush()
        })
    }

    @Test
    fun testUpdateEmployees() {
        // Given
        val employee = EmployeeController.EmployeePutDTO(
            userId = null,
            surname = "Ford",
            firstName = "Michaela",
            contactNo = "12345678901",
            status = Employee.EmployeeStatus.IN,
            middleName = null,
            suffix = null,
            profilePictureId = null
        )
        val id = UUID.fromString("019ba2a9-85e9-7fed-858c-e921c11547ad")
        // When
        val updated = employeeServiceImpl.updateEmployee(id, employee)
        // Then
        assertNotNull(updated.actorId)
        assertEquals("Ford", updated.surname)
        assertEquals("12345678901", updated.contactNo)
        assertEquals(Employee.EmployeeStatus.IN, updated.status)
    }

    @Test
    fun testDeleteEmployee() {
        // Then
        val id = UUID.fromString("019ba2a9-85e9-7fed-858c-e921c11547ad")
        val count = employeeRepository.count()
        // When
        val employee = employeeServiceImpl.deleteEmployee(id)
        // Then
        assertEquals(count - 1, employeeRepository.count())
        assertEquals(null, employeeRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            employeeServiceImpl.deleteEmployee(id)
        }
    }
}