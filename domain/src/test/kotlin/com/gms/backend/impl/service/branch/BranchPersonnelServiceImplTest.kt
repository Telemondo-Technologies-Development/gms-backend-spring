package com.gms.backend.impl.service.branch

import com.gms.backend.domain.application.rest.branch.BranchPersonnelController
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.repository.branch.BranchPersonnelRepository
import com.gms.backend.domain.impl.domain.service.branch.BranchPersonnelServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.test.annotation.DirtiesContext
import java.util.*
import kotlin.jvm.optionals.getOrNull

class BranchPersonnelServiceImplTest
@Autowired constructor(
    private val branchPersonnelServiceImpl: BranchPersonnelServiceImpl,
    private val branchPersonnelRepository: BranchPersonnelRepository,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadBranchPersonnel() {
        // When
        val result = branchPersonnelServiceImpl.getBranchPersonnel(Pageable.unpaged())

        // Then
        assertEquals(3, result.totalElements)
    }

    @Test
    fun testGetBranchPersonnelById() {
        // Given
        val id = UUID.fromString("019ba2b8-df68-7791-99d6-d4181d6c2950")

        // When
        val result = branchPersonnelServiceImpl.getBranchPersonnelById(id)

        // Then
        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals(BranchPersonnel.BranchPersonnelStatus.ACTIVE, result.status)
    }

    @Test
    fun testCreateBranchPersonnel() {
        // Given
        val actorId = UUID.fromString("e79d0924-2c4c-49dd-b2df-bbfad0fae629")
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val body = BranchPersonnelController.BranchPersonnelPostDTO(
            actorId = actorId,
            branchId = branchId,
            status = BranchPersonnel.BranchPersonnelStatus.ACTIVE,
            createdById = adminId
        )

        // When
        val saved = branchPersonnelServiceImpl.createBranchPersonnel(body)

        // Then
        assertNotNull(saved.id)
        assertEquals(branchId, saved.branchId)
        assertEquals(4, branchPersonnelRepository.count())
    }

    @Test
    fun testUpdateBranchPersonnel() {
        // Given
        val id = UUID.fromString("019ba2b8-df68-7791-99d6-d4181d6c2950")
        val actorId = UUID.fromString("e79d0924-2c4c-49dd-b2df-bbfad0fae629")
        val branchId = UUID.fromString("019ba297-802d-78e4-804a-cbed3a77e6ce")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")

        val body = BranchPersonnelController.BranchPersonnelPutDTO(
            actorId = actorId,
            branchId = branchId,
            status = BranchPersonnel.BranchPersonnelStatus.UNDECIDED,
            updatedById = adminId
        )

        // When
        val updated = branchPersonnelServiceImpl.updateBranchPersonnel(id, body)

        // Then
        assertEquals(id, updated.id)
        assertEquals(BranchPersonnel.BranchPersonnelStatus.UNDECIDED, updated.status)
        assertEquals(branchId, updated.branchId)
        assertEquals(actorId, updated.actorId)
    }

    @Test
    fun testDeleteBranchPersonnel() {
        // Given
        val id = UUID.fromString("019c1bfa-e7e1-77f5-bf7f-c344d7680e77")
        val initialCount = branchPersonnelRepository.count()

        // When
        branchPersonnelServiceImpl.deleteBranchPersonnel(id)

        // Then
        assertEquals(initialCount - 1, branchPersonnelRepository.count())
        assertEquals(null, branchPersonnelRepository.findById(id).getOrNull())

        assertThrows<NoSuchElementException> {
            branchPersonnelServiceImpl.deleteBranchPersonnel(id)
        }
    }
}