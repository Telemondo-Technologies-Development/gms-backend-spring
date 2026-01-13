package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.user.EmployeeMapper
import com.gms.backend.domain.application.rest.user.EmployeeController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.EmployeeRepository
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.user.EmployeeService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val employeeMapper: EmployeeMapper,
    private val userRepository: UserRepository,
    private val objectRepository: ObjectStorageRepository
) : EmployeeService {
    @Transactional
    @PreAuthorize("hasAuthority('employee_create')")
    override fun createEmployee(body: EmployeeController.EmployeePostDTO): EmployeeController.EmployeeTableDTO {
        val employee = employeeMapper.employeePostDTOToEmployee(body).apply {
            actor = Actor().apply {
                type = Actor.ActorType.EMPLOYEE
                status = Actor.ActorStatus.ACTIVE
            }
            user = body.userId?.let { userRepository.getReferenceById(it) }
            profilePicture = body.profilePictureId?.let { objectRepository.getReferenceById(it) }
        }

        val saved = employeeRepository.save(employee)
        return employeeMapper.employeeToEmployeeTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('employee_read')")
    override fun getEmployees(): List<EmployeeController.EmployeeTableDTO> {
        return employeeRepository.findAll().let(employeeMapper::employeesToEmployeeTableDTO)
//        return employeeRepository.findAllProjectedBy()
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('employee_read') and hasAuthority('user_read')")
    override fun getEmployeeById(id: UUID): EmployeeController.EmployeeTableDTO {
        return employeeRepository.findById(id).orElseThrow().let(employeeMapper::employeeToEmployeeTableDTO)
    }

    @Transactional
    @PreAuthorize("hasAuthority('employee_update')")
    override fun updateEmployee(
        id: UUID, body: EmployeeController.EmployeePutDTO
    ): EmployeeController.EmployeeTableDTO {
        val employee = employeeRepository.findById(id).orElseThrow {
            NoSuchElementException("Employee not found with ID: $id")
        }.apply {
            employeeMapper.employeePutDTOToEmployee(body, this)
            this.id = id // Explicit 'this' helps clarity when parameter name matches property name
            user = body.userId?.let { userRepository.getReferenceById(it) }
            profilePicture = body.profilePictureId?.let { objectRepository.getReferenceById(it) }
        }

        employeeRepository.save(employee)
        return employeeMapper.employeeToEmployeeTableDTO(employee)
    }

    @Transactional
    @PreAuthorize("hasAuthority('employee_delete')")
    override fun deleteEmployee(id: UUID) {
        val employee = employeeRepository.findById(id).orElseThrow().apply {
            actor.status = Actor.ActorStatus.DELETED
            actor.deactivatedAt = Instant.now()
        }

        employeeRepository.delete(employee)
    }

}