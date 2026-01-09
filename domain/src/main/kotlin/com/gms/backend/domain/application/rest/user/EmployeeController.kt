package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.mapper.user.EmployeeMapper
import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.impl.domain.service.user.EmployeeServiceImpl
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/employee")
class EmployeeController(
    private val employeeService: EmployeeServiceImpl, private val employeeMapper: EmployeeMapper
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
    fun getAllUsers() = employeeService.getEmployees().toOkResponse()

    @GetMapping("/{id}")
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
    fun createEmployee(@RequestBody body: EmployeePostDTO) = employeeService.createEmployee(body).toCreatedResponse()

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
    fun updateEmployee(@PathVariable id: UUID, @RequestBody body: EmployeePutDTO) =
        employeeService.updateEmployee(id, body).toOkResponse()

    @DeleteMapping("/{id}")
    fun deleteEmployee(@PathVariable id: UUID) = employeeService.deleteEmployee(id).toOkResponse("Employee Deleted")
}