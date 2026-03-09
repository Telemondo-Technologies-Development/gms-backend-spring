package com.gms.backend.domain.domain.repository.member

import com.gms.backend.domain.application.rest.member.AttendanceController
import com.gms.backend.domain.domain.model.member.Attendance
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface AttendanceRepository : JpaRepository<Attendance, UUID> {
    @Query(
        value = $$"""
    SELECT new com.gms.backend.domain.application.rest.member.AttendanceController$AttendanceTableDTO(
        a.id,
        a.actorId,
        a.branchId,
        a.source,
        a.type,
        a.recordedAt,
        a.createdById,
        a.updatedById
    )
    FROM Attendance a
    WHERE (:source IS NULL OR a.source = :source)
      AND (:type IS NULL OR a.type = :type)
      AND (:dateFrom IS NULL OR a.recordedAt >= :dateFrom)
      AND (:dateTo IS NULL OR a.recordedAt <= :dateTo)
    """,
        countQuery = """
    SELECT COUNT(a) FROM Attendance a
    WHERE (:source IS NULL OR a.source = :source)
      AND (:type IS NULL OR a.type = :type)
      AND (:dateFrom IS NULL OR a.recordedAt >= :dateFrom)
      AND (:dateTo IS NULL OR a.recordedAt <= :dateTo)
    """
    )
    fun findAllProjectedBy(
        pageable: Pageable,
        @Param("source") source: Attendance.AttendanceSource?,
        @Param("type") type: Attendance.AttendanceType?,
        @Param("dateFrom") dateFrom: Instant?,
        @Param("dateTo") dateTo: Instant?
    ): Page<AttendanceController.AttendanceTableDTO>

    @Query(
        value = $$"""
    SELECT new com.gms.backend.domain.application.rest.member.AttendanceController$AttendanceTableDTO(
        a.id,
        a.actorId,
        a.branchId,
        a.source,
        a.type,
        a.recordedAt,
        a.createdById,
        a.updatedById
    )
    FROM Attendance a
    WHERE a.id = :id
      AND(:source IS NULL OR a.source = :source)
      AND (:type IS NULL OR a.type = :type)
      AND (:dateFrom IS NULL OR a.recordedAt >= :dateFrom)
      AND (:dateTo IS NULL OR a.recordedAt <= :dateTo)
    """
    )
    fun findByAttendanceId(
        @Param("id") id: UUID,
        @Param("source") source: Attendance.AttendanceSource?,
        @Param("type") type: Attendance.AttendanceType?,
        @Param("dateFrom") dateFrom: Instant?,
        @Param("dateTo") dateTo: Instant?
    ): Optional<AttendanceController.AttendanceTableDTO>
}
