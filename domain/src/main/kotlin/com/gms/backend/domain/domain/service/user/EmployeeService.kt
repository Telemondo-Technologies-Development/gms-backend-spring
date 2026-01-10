package com.gms.backend.domain.domain.service.user

import com.gms.backend.domain.application.rest.user.EmployeeController
import java.util.*

interface EmployeeService {
    fun createEmployee(body: EmployeeController.EmployeePostDTO): EmployeeController.EmployeeTableDTO
    fun getEmployees(): List<EmployeeController.EmployeeTableDTO>
    fun getEmployeeById(id: UUID): EmployeeController.EmployeeTableDTO
    fun updateEmployee(id: UUID, body: EmployeeController.EmployeePutDTO): EmployeeController.EmployeeTableDTO
    fun deleteEmployee(id: UUID)
}