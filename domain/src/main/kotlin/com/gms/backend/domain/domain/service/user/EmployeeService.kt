package com.gms.backend.domain.domain.service.Employee

import com.gms.backend.domain.application.rest.user.EmployeeController
import com.gms.backend.domain.domain.model.user.Employee
import java.util.*

interface EmployeeService {
    fun createEmployee(body: EmployeeController.EmployeePostDTO)
    fun getEmployees(): List<EmployeeController.EmployeeTableDTO>
    fun getEmployeeById(id: UUID): EmployeeController.EmployeeTableDTO
    fun updateEmployee(id: UUID, body: EmployeeController.EmployeePutDTO)
    fun deleteEmployee(id: UUID)
}