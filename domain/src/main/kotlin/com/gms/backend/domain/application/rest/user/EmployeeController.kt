package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.ApiResponse
import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.application.rest.storage.ObjectStorageController
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import com.gms.backend.domain.domain.model.user.Employee
import com.gms.backend.domain.domain.service.storage.ObjectStorageService
import com.gms.backend.domain.domain.service.user.EmployeeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/employee")
@Tag(name = "Employee")
class EmployeeController(
    private val employeeService: EmployeeService,
    private val storageService: ObjectStorageService,
    private val bucketConfig: ObjectStorageController.MinioBucketConfig
) {

    @Schema(description = "Format for Employee read")
    data class EmployeeTableDTO(
        val id: UUID,
        val actorId: UUID?,
        val userId: UUID?,
        val username: String?,
        val surname: String,
        val firstName: String,
        val middleName: String?,
        val suffix: String?, // Might set to enum
        val contactNo: String,
        val status: Employee.EmployeeStatus,
    ) {
        var branches: List<BranchesBriefDTO> = emptyList()
    }

    data class BranchesWithEmployeeId(
        val actorId: UUID,
        val id: UUID,
        val name: String
    )

    data class BranchesBriefDTO(
        val id: UUID,
        val name: String
    )

    @Schema(description = "Format for Employee create")
    data class EmployeePostDTO(
        val userId: UUID?,
        @field:NotBlank(message = "Surname must not be empty")
        val surname: String,
        @field:NotBlank(message = "First Name must not be empty")
        val firstName: String,
        @Size(min = 1, message = "Middle name cannot be blank if provided")
        val middleName: String?,
        @Size(min = 1, message = "Middle name cannot be blank if provided")
        val suffix: String?,
        @field:Size(min = 11, max = 25, message = "Must be a valid contact no.")
        val contactNo: String,
        val status: Employee.EmployeeStatus,
        val profilePictureId: UUID?
    )

    @Schema(description = "Format for Employee update")
    data class EmployeePutDTO(
        val userId: UUID?,
        @field:NotBlank(message = "Surname must not be empty")
        val surname: String,
        @field:NotBlank(message = "Surname must not be empty")
        val firstName: String,
        @Size(min = 1, message = "Middle name cannot be blank if provided")
        val middleName: String?,
        @Size(min = 1, message = "Suffix cannot be blank if provided")
        val suffix: String?,
        @field:Size(min = 11, max = 25, message = "Must be a valid contact no.")
        val contactNo: String,
        val status: Employee.EmployeeStatus,
        val profilePictureId: UUID?
    )

    @GetMapping
    @Operation(summary = "Get all Employees")
    fun getAllEmployees(pageable: Pageable) = employeeService.getEmployees(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get an Employee by id")
    fun getEmployee(@PathVariable id: UUID) = employeeService.getEmployeeById(id).toOkResponse()

    @PostMapping
    @Operation(summary = "Create a new Employee")
    fun createEmployee(@Valid @RequestBody body: EmployeePostDTO) =
        employeeService.createEmployee(body).toCreatedResponse()

    @PutMapping("/{id}")
    @Operation(summary = "Update an Employee by id")
    fun updateEmployee(@PathVariable id: UUID, @Valid @RequestBody body: EmployeePutDTO) =
        employeeService.updateEmployee(id, body).toOkResponse()

    @PostMapping("/picture")
    @Operation(summary = "Upload an employee profile picture into the object storage (public)")
    fun uploadEmployeeProfile(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponse<ObjectStorage>> =
        storageService.uploadFile(file, bucketConfig.public, "profiles/employees", storageService.getCurrentActor())
            .toCreatedResponse("Employee profile picture uploaded successfully")

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an Employee by id")
    fun deleteEmployee(@PathVariable id: UUID) = employeeService.deleteEmployee(id).toOkResponse("Employee Deleted")
}