package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.application.rest.user.EmployeeController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface EmployeeService {
    fun createEmployee(body: EmployeeController.EmployeePostDTO): EmployeeController.EmployeeTableDTO
    fun getEmployees(pageable: Pageable): Page<EmployeeController.EmployeeTableDTO>
    fun getEmployeeById(id: UUID): EmployeeController.EmployeeTableDTO
    fun updateEmployee(id: UUID, body: EmployeeController.EmployeePutDTO): EmployeeController.EmployeeTableDTO
    fun deleteEmployee(id: UUID)
}