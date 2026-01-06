package com.gms.backend.domain.impl.domain.service.user

import com.gms.backend.domain.application.mapper.EmployeeMapper
import com.gms.backend.domain.application.rest.EmployeeController
import com.gms.backend.domain.domain.model.user.Actor
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.domain.repository.storage.ObjectStorageRepository
import com.gms.backend.domain.domain.repository.user.EmployeeRepository
import com.gms.backend.domain.domain.repository.user.UserRepository
import com.gms.backend.domain.domain.service.Employee.EmployeeService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmployeeServiceImpl(
    private val employeeRepository: EmployeeRepository,
    private val employeeMapper: EmployeeMapper,
    private val userRepository: UserRepository,
    private val objectRepository: ObjectStorageRepository
) : EmployeeService {
    @Transactional
    override fun createEmployee(body: EmployeeController.EmployeePostDTO): Employee {
        val employee = employeeMapper.employeePostDTOToEmployee(body)
        employee.actor = Actor().let { it.type = Actor.ActorType.EMPLOYEE; it }
        body.userId?.let {
            employee.user = userRepository.getReferenceById(it)
        }
        body.profilePictureId?.let {
            employee.profilePicture = objectRepository.getReferenceById(it)
        }
        return employeeRepository.save(employee)
    }

    @Transactional
    override fun getEmployees(): List<EmployeeController.EmployeeTableDTO> {
        val employees = employeeRepository.findAll()
        return employeeMapper.employeesToDTO(employees)
//        return employeeRepository.findAllProjectedBy()
    }

    @Transactional
    override fun getEmployeeById(id: UUID): Optional<Employee> {
        return employeeRepository.findById(id)
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
        return employeeRepository.deleteById(id)
    }

}