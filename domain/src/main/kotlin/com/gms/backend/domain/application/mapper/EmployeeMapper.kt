package com.gms.backend.domain.application.mapper

import com.gms.backend.domain.application.rest.EmployeeController
import com.gms.backend.domain.domain.model.user.Employee
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface EmployeeMapper {
    fun employeeToEmployeeTableDTO(employee: Employee): EmployeeController.EmployeeTableDTO
    fun employeePostDTOToEmployee(dto: EmployeeController.EmployeePostDTO): Employee
    fun employeePutDTOToEmployee(dto: EmployeeController.EmployeePutDTO, @MappingTarget employee: Employee): Employee
    fun employeesToEmployeeTableDTO(employees: List<Employee>): List<EmployeeController.EmployeeTableDTO>
}