package com.gms.backend.domain.domain.repository.user

import com.gms.backend.domain.application.rest.user.EmployeeController
import com.gms.backend.domain.domain.model.user.Employee
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface EmployeeRepository : JpaRepository<Employee, UUID> {
    fun findAllByEmployeeObjectsId(id: UUID): List<Employee>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.user.EmployeeController$EmployeeTableDTO(
        e.id,
        e.actorId,
        e.userId,
        u.email as username,
        e.surname,
        e.firstName,
        e.middleName,
        e.suffix,
        e.contactNo,
        e.status
        )
        FROM Employee e
        LEFT JOIN e.user u
        ORDER BY e.createdAt DESC
        """,
        countQuery = "SELECT COUNT(e) FROM Employee e"
    )
    fun findAllProjectedBy(pageable: Pageable): Page<EmployeeController.EmployeeTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.user.EmployeeController$EmployeeTableDTO(
        e.id,
        e.actorId,
        e.userId,
        u.email as username,
        e.surname,
        e.firstName,
        e.middleName,
        e.suffix,
        e.contactNo,
        e.status
        )
        FROM Employee e
        LEFT JOIN e.user u
        WHERE e.id = :id
        ORDER BY e.createdAt DESC
        """
    )
    fun findByEmployeeId(id: UUID): Optional<EmployeeController.EmployeeTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.user.EmployeeController$BranchesWithEmployeeId(
        bp.actorId,
        b.id,
        b.name
        )
        FROM BranchPersonnel bp
        JOIN bp.branch b
        WHERE bp.actorId IN :employeeIds
        ORDER BY bp.actorId
        """
    )
    fun findAllBranchesByIds(employeeIds: List<UUID>): List<EmployeeController.BranchesWithEmployeeId>

    fun findAllByUserIdIn(userIds: List<UUID>): List<Employee>
    fun findAllByActorIdIn(actorIds: List<UUID>): List<Employee>
}
