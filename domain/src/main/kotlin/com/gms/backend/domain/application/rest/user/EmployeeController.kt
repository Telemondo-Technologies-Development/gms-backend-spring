package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.mapper.user.EmployeeMapper
import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.impl.domain.service.user.EmployeeServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/employee")
@Tag(name = "Employee")
class EmployeeController(
    private val employeeService: EmployeeServiceImpl, private val employeeMapper: EmployeeMapper
) {

    @Schema(description = "Format for Employee read")
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
    @Operation(summary = "Get all Employees")
    fun getAllUsers() = employeeService.getEmployees().toOkResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an Employee by id")
    fun getEmployee(@PathVariable id: UUID) = employeeService.getEmployeeById(id).toOkResponse()

    @Schema(description = "Format for Employee create")
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
    @Operation(summary = "Create a new Employee")
    fun createEmployee(@RequestBody body: EmployeePostDTO) = employeeService.createEmployee(body).toCreatedResponse()

    @Schema(description = "Format for Employee update")
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
    @Operation(summary = "Update an Employee by id")
    fun updateEmployee(@PathVariable id: UUID, @RequestBody body: EmployeePutDTO) =
        employeeService.updateEmployee(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Employee by id")
    fun deleteEmployee(@PathVariable id: UUID) = employeeService.deleteEmployee(id).toOkResponse("Employee Deleted")
}