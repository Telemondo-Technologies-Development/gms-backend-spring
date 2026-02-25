package com.gms.backend.impl.service.asset

import com.gms.backend.domain.application.rest.asset.MaintenanceScheduleController
import com.gms.backend.domain.domain.repository.asset.MaintenanceScheduleRepository
import com.gms.backend.domain.impl.domain.service.asset.MaintenanceScheduleServiceImpl
import com.gms.backend.domain.infra.quartz.asset.MaintenanceSchedulerService
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.jvm.optionals.getOrNull

class MaintenanceScheduleServiceImplTest @Autowired constructor(
    private val maintenanceScheduleServiceImpl: MaintenanceScheduleServiceImpl,
    private val maintenanceScheduleRepository: MaintenanceScheduleRepository,
    private val nc: Connection
) : BaseTest() {

    @MockitoBean
    private lateinit var maintenanceSchedulerService: MaintenanceSchedulerService

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadMaintenanceSchedules() {
        val schedules = maintenanceScheduleServiceImpl.getSchedules(Pageable.unpaged())
        assertEquals(8, schedules.totalElements)
    }

    @Test
    fun testGetMaintenanceScheduleById() {
        val id = UUID.fromString("019ba2f9-c6e6-7271-893c-ffab220066c1") // Weekly maintenance
        val result = maintenanceScheduleServiceImpl.getScheduleById(id)

        assertNotNull(result)
        assertEquals("Weekly Maintenance", result.name)
    }

    @Test
    fun testCreateMaintenanceSchedule() {
        // Given
        val assetId = UUID.fromString("019ba2f5-b6e6-7271-893c-ffab220055b1") //Cardio
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val body = MaintenanceScheduleController.SchedulePostDTO(
            name = "New Maintenance Schedule",
            assetId = assetId,
            startDate = Instant.now().plus(1, ChronoUnit.DAYS),
            intervalUnit = ChronoUnit.MONTHS,
            intervalValue = 3,
            leadTimeHours = 48,
            timeToCompleteHours = 4,
            weekRank = null,
            dayOfWeek = null,
            monthOfYear = null,
            createdById = adminId
        )

        // When
        val saved = maintenanceScheduleServiceImpl.createSchedule(body)

        // Then
        assertNotNull(saved.id)
        assertEquals("New Maintenance Schedule", saved.name)

        // Checks if the service tried to call processSchedules (in MaintenanceSchedulerService)
        verify(maintenanceSchedulerService, atLeastOnce()).processSchedules()
    }

    @Test
    fun testUpdateMaintenanceSchedule() {
        // Given
        val id = UUID.fromString("019ba2f9-c6e6-7271-893c-ffab2200bbb1") //Old Advanced Monthly
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val body = MaintenanceScheduleController.SchedulePutDTO(
            name = "Updated Monthly Plan",
            startDate = Instant.now().plus(2, ChronoUnit.DAYS),
            intervalUnit = ChronoUnit.MONTHS,
            intervalValue = 1,
            leadTimeHours = 0,
            timeToCompleteHours = 2,
            weekRank = 1,
            dayOfWeek = 1,
            monthOfYear = null,
            active = false,
            updatedById = adminId
        )

        // When
        val updated = maintenanceScheduleServiceImpl.updateSchedule(id, body)

        // Then
        assertEquals("Updated Monthly Plan", updated.name)

        // Checks if the service tried to call cancelFutureWorkers and processSchedules
        verify(maintenanceSchedulerService).cancelFutureWorkers(id)
        verify(maintenanceSchedulerService, atLeastOnce()).processSchedules()
    }

    @Test
    fun testDeleteMaintenanceSchedule() {
        // Given
        val id = UUID.fromString("019ba2f9-c6e6-7271-893c-ffab22009999")
        val initialCount = maintenanceScheduleRepository.count()

        // When
        maintenanceScheduleServiceImpl.deleteSchedule(id)

        // Then
        assertEquals(initialCount - 1, maintenanceScheduleRepository.count())
        assertEquals(null, maintenanceScheduleRepository.findById(id).getOrNull())

        // Checks if the service tried to call cancelFutureWorkers
        verify(maintenanceSchedulerService).cancelFutureWorkers(id)

        assertThrows<NoSuchElementException> {
            maintenanceScheduleServiceImpl.getScheduleById(id)
        }
    }
}