package com.gms.backend.domain.domain.service.Employee

import com.gms.backend.domain.application.rest.EmployeeController
import com.gms.backend.domain.domain.model.user.Employee
import java.util.Optional
import java.util.UUID

interface EmployeeService {
    fun createEmployee(body: EmployeeController.EmployeePostDTO): Employee
    fun getEmployees(): List<EmployeeController.EmployeeTableDTO>
    fun getEmployeeById(id: UUID): Optional<Employee>
    fun updateEmployee(id: UUID, body: EmployeeController.EmployeePutDTO)
    fun deleteEmployee(id: UUID)
}