package com.gms.backend.domain.application.rest

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.impl.domain.service.user.EmployeeServiceImpl
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/employee")
class EmployeeController(
    private val employeeService: EmployeeServiceImpl
) {

    data class EmployeeTableDTO(
        val id: UUID,
        val user: UserController.UserTableDTO?,
        val actorId: UUID?,
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val contactNo: String,
        val status: Employee.EmployeeStatus,
    )

    @GetMapping
    @Operation(summary = "Get all employees")
    fun getAllUsers() = employeeService.getEmployees().toOkResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an employee by id")
    fun getEmployee(@PathVariable id: UUID) = employeeService.getEmployeeById(id).toOkResponse()

    data class EmployeePostDTO(
        val userId: UUID?,
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val contactNo: String,
        val status: Employee.EmployeeStatus,
        val profilePictureId: UUID?
    )

    @PostMapping
    @Operation(summary = "Create a new employee")
    fun createEmployee(@RequestBody body: EmployeePostDTO) =
        employeeService.createEmployee(body).toCreatedResponse()

    data class EmployeePutDTO(
        val userId: UUID?,
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val contactNo: String,
        val status: Employee.EmployeeStatus,
        val profilePictureId: UUID?
    )

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee by id")
    fun updateEmployee(@PathVariable id: UUID, @RequestBody body: EmployeePutDTO) =
        employeeService.updateEmployee(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee by id")
    fun deleteEmployee(@PathVariable id: UUID) =
        employeeService.deleteEmployee(id).toOkResponse("Employee Deleted")
}