package com.gms.backend.domain.domain.repository.member.report

import com.gms.backend.domain.application.rest.member.report.ReportController
import com.gms.backend.domain.domain.model.member.report.Report
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ReportRepository : JpaRepository<Report, UUID> {

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.member.report.ReportController$ReportTableDTO(
                r.id,
                r.reportType.id,
                r.description,
                r.occurredAt,

                b.id,
                b.name,
                b.address,
                b.status,
                
                a.id,
                m.surname,
                m.firstName,
                m.status,
        
                r.createdById,
                ca.type,
                cu.email,
                ce.surname,
                ce.firstName,
        
                r.updatedById,
                ua.type,
                uu.email,
                ue.surname,
                ue.firstName,
        
                r.createdAt,
                r.updatedAt
            )
            FROM Report r 
            JOIN r.branch b 
            JOIN r.actor a 
            LEFT JOIN Member m ON m.actor = a
            JOIN r.createdBy ca 
            LEFT JOIN User cu ON ca = cu.actor 
            LEFT JOIN cu.userEmployees ce 
            JOIN r.updatedBy ua 
            LEFT JOIN User uu ON ua = uu.actor 
            LEFT JOIN uu.userEmployees ue
        """, countQuery = "SELECT COUNT(r) FROM Report r")
    fun findAllProjectedBy(pageable: Pageable): Page<ReportController.ReportTableDTO>

    @Query($$"""
        SELECT new com.gms.backend.domain.application.rest.member.report.ReportController$ReportObjectMappingDTO(
            r.id, 
            o.id
        )
        FROM Report r 
        JOIN r.reportsObjects o 
        WHERE r.id IN :reportIds
    """)
    fun findAllObjectIdsByReportIds(@Param("reportIds") reportIds: List<UUID>): List<ReportController.ReportObjectMappingDTO>

    @Query(
        value =$$"""
        SELECT new com.gms.backend.domain.application.rest.member.report.ReportController$ReportTableDTO(
                r.id,
                r.reportType.id,
                r.description,
                r.occurredAt,

                b.id,
                b.name,
                b.address,
                b.status,
                
                a.id,
                m.surname,
                m.firstName,
                m.status,
        
                r.createdById,
                ca.type,
                cu.email,
                ce.surname,
                ce.firstName,
        
                r.updatedById,
                ua.type,
                uu.email,
                ue.surname,
                ue.firstName,
        
                r.createdAt,
                r.updatedAt
            )
            FROM Report r 
            JOIN r.branch b 
            JOIN r.actor a 
            LEFT JOIN Member m ON m.actor = a
            JOIN r.createdBy ca 
            LEFT JOIN User cu ON ca = cu.actor 
            LEFT JOIN cu.userEmployees ce 
            JOIN r.updatedBy ua 
            LEFT JOIN User uu ON ua = uu.actor 
            LEFT JOIN uu.userEmployees ue
            WHERE r.id = :id
        """)
    fun findProjectedBy(@Param("id") id: UUID): Optional<ReportController.ReportTableDTO>

    @Query("""
        SELECT o.id 
            FROM Report r 
            JOIN r.reportsObjects o 
            WHERE r.id = :reportId
        """)
    fun findObjectIdsByReportId(@Param("reportId") reportId: UUID): List<UUID>

    fun findAllByReportsObjectsId(id: UUID): List<Report>
    fun findAllByActorId(actorId: UUID, pageable: Pageable): Page<Report>
}
