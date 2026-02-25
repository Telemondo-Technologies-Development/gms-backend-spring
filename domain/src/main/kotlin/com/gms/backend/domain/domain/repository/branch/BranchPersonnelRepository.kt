package com.gms.backend.domain.domain.repository.branch

import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.model.user.Employee
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BranchPersonnelRepository : JpaRepository<BranchPersonnel, UUID> {
    fun findAllByBranchId(branchId: UUID, pageable: Pageable): Page<BranchPersonnel>
    fun findAllByBranchIdAndStatus(
        branchId: UUID,
        status: BranchPersonnel.BranchPersonnelStatus,
        pageable: Pageable
    ): Page<BranchPersonnel>
    @Query("""
    SELECT new com.gms.backend.domain.application.rest.branch.BranchController${'$'}EmployeeInBranchDTO(
        bp.actorId,
        e.id,
        e.surname,
        e.firstName,
        e.middleName,
        e.suffix,
        e.contactNo,
        pr.id,
        pr.name,
        pr.description
    )
    FROM BranchPersonnel bp
    LEFT JOIN Employee e ON e.actorId = bp.actorId
    LEFT JOIN bp.personnelRole pr
    WHERE bp.branch.id = :branchId
    AND (:bpStatus IS NULL OR bp.status = :bpStatus)
    AND (:eStatus IS NULL OR e.status = :eStatus)
""")
    fun findAllEmployeesByBranchIdAndStatus(
        @Param("branchId") branchId: UUID,
        @Param("bpStatus") bpStatus: BranchPersonnel.BranchPersonnelStatus?,
        @Param("eStatus") eStatus: Employee.EmployeeStatus?,
        pageable: Pageable
    ): Page<BranchController.EmployeeInBranchDTO>
}