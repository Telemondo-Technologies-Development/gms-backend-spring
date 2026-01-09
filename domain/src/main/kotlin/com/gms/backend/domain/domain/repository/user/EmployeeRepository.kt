package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.application.rest.user.EmployeeController
import com.gms.backend.domain.domain.model.user.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface EmployeeRepository : JpaRepository<Employee, UUID> {
    fun findAllByEmployeeObjectsId(id: UUID): List<Employee>
    // Fix when needed
    fun findAllProjectedBy(): List<EmployeeController.EmployeeTableDTO>
}
