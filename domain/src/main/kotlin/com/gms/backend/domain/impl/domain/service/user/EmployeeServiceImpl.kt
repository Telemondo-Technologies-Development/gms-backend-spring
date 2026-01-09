package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.user.EmployeeMapper
import com.gms.backend.domain.application.rest.user.EmployeeController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.EmployeeRepository
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.Employee.EmployeeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val employeeMapper: EmployeeMapper,
    private val userRepository: UserRepository,
    private val objectRepository: ObjectStorageRepository
) : EmployeeService {
    @Transactional
    override fun createEmployee(body: EmployeeController.EmployeePostDTO) {
        val employee = employeeMapper.employeePostDTOToEmployee(body)
        employee.actor = Actor().let { it.type = Actor.ActorType.EMPLOYEE; it.status = Actor.ActorStatus.ACTIVE; it }
        body.userId?.let {
            employee.user = userRepository.getReferenceById(it)
        }
        body.profilePictureId?.let {
            employee.profilePicture = objectRepository.getReferenceById(it)
        }
        employeeRepository.save(employee)
    }

    @Transactional(readOnly = true)
    override fun getEmployees(): List<EmployeeController.EmployeeTableDTO> {
        return employeeRepository.findAll().let(employeeMapper::employeesToEmployeeTableDTO)
//        return employeeRepository.findAllProjectedBy()
    }

    @Transactional(readOnly = true)
    override fun getEmployeeById(id: UUID): EmployeeController.EmployeeTableDTO {
        return employeeRepository.findById(id).orElseThrow().let(employeeMapper::employeeToEmployeeTableDTO)
    }

    @Transactional
    override fun updateEmployee(id: UUID, body: EmployeeController.EmployeePutDTO) {
        val employee = employeeRepository.findById(id).orElseThrow {
            NoSuchElementException("Employee not found with ID: $id")
        }
        employeeMapper.employeePutDTOToEmployee(body, employee)
        employee.id = id
        // Let the db handle the fk check
        body.userId?.let {
            employee.user = userRepository.getReferenceById(it)
        }
        body.profilePictureId?.let {
            employee.profilePicture = objectRepository.getReferenceById(it)
        }
    }

    @Transactional
    override fun deleteEmployee(id: UUID) {
        val employee = employeeRepository.findById(id).orElseThrow()
        employee.actor?.status = Actor.ActorStatus.DELETED
        employee.actor?.deactivatedAt = Instant.now()
        employeeRepository.delete(employee)
    }

}