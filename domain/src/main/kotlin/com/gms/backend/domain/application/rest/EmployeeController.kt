package com.gms.backend.domain.application.rest

import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.impl.domain.service.user.EmployeeServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @GetMapping("")
    fun getAllUsers(): ResponseEntity<List<EmployeeTableDTO>> {
        val employees = employeeService.getEmployees()
        return ResponseEntity.ok(employees)
    }

    @GetMapping("/{id}")
    fun getEmployee(@PathVariable id: UUID): ResponseEntity<Optional<Employee>> {
        val employee = employeeService.getEmployeeById(id)
        return ResponseEntity.ok(employee)
    }

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

    @PostMapping("")
    fun createEmployee(@RequestBody body: EmployeePostDTO): ResponseEntity<String> {
        val something = employeeService.createEmployee(body)
        return ResponseEntity.ok("Employee Successfully Created!")
    }

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
    fun updateEmployee(@PathVariable id: UUID, @RequestBody body: EmployeePutDTO): ResponseEntity<String> {
        return try {
            employeeService.updateEmployee(id, body)
            ResponseEntity.ok("Employee updated")
        } catch (e: Exception) {
            // Catch all other exceptions
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating employee: ${e.message}")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteEmployee(@PathVariable id: UUID): ResponseEntity<String> {
        return try {
            employeeService.deleteEmployee(id)
            ResponseEntity.ok("Employee Deleted")
        } catch (e: Exception) {
            // Catch all other exceptions
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting employee: ${e.message}")
        }
    }
}