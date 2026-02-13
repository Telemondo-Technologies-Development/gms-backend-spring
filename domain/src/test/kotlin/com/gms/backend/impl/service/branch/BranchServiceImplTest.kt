package com.gms.backend.impl.service.branch

import com.gms.backend.domain.application.rest.branch.BranchController
import com.gms.backend.domain.domain.model.branch.Branch
import com.gms.backend.domain.domain.model.branch.BranchPersonnel
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.impl.domain.service.branch.BranchServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.test.annotation.DirtiesContext
import java.util.*
import kotlin.jvm.optionals.getOrNull

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BranchServiceImplTest
@Autowired constructor(
    private val branchServiceImpl: BranchServiceImpl,
    private val branchRepository: BranchRepository,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(Connection.Status.CONNECTED, nc.status)
    }

    @Test
    fun testReadBranches() {
        // When
        val branches = branchServiceImpl.getBranches(Pageable.unpaged())

        // Then
        assertEquals(3, branches.totalElements)
    }

    @Test
    fun testGetBranchById() {
        // Given
        val id = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")

        // When
        val result = branchServiceImpl.getBranchById(id)

        // Then
        assertNotNull(result)
        assertEquals("Matina", result.name)
    }

    @Test
    fun testCreateBranch() {
        // Given
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = BranchController.BranchPostDTO(
            name = "New Branch",
            address = "New Address",
            longitude = "125.123456",
            latitude = "125.123456",
            status = Branch.BranchStatus.ACTIVE,
            profilePictureId = null,
            createdById = adminId
        )

        // When
        val saved = branchServiceImpl.createBranch(body)

        // Then
        assertNotNull(saved.id)
        assertEquals("New Branch", saved.name)
        assertEquals(4, branchRepository.count())
    }

    @Test
    fun testUpdateBranch() {
        // Given
        val id = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")
        val adminId = UUID.fromString("f520a8fb-3824-4339-8bb4-3732c8a3f617")
        val body = BranchController.BranchPutDTO(
            name = "Updated Branch",
            address = "Updated Address",
            longitude = "125.123456",
            latitude = "125.123456",
            status = Branch.BranchStatus.UNDECIDED,
            profilePictureId = null,
            updatedById = adminId
        )

        // When
        val updated = branchServiceImpl.updateBranch(id, body)

        // Then
        assertEquals("Updated Branch", updated.name)
        assertEquals(id, updated.id)
    }

    @Test
    fun testDeleteBranch() {
        // Given
        val id = UUID.fromString("019ba297-802d-78e4-804a-cbed3a77e6ce")
        val initialCount = branchRepository.count()

        // When
        branchServiceImpl.deleteBranch(id)

        // Then
        assertEquals(initialCount - 1, branchRepository.count())
        assertEquals(null, branchRepository.findById(id).getOrNull())
    }

    @Test
    fun testGetBranchEmployees() {
        // Given
        val branchId = UUID.fromString("019ba279-a6e6-7271-893c-ffab220040a2")

        // When
        val result = branchServiceImpl.getBranchEmployees(branchId, BranchPersonnel.BranchPersonnelStatus.ACTIVE)

        // Then
        assertNotNull(result.branch)
        assertEquals("Matina", result.branch.name)
        assertNotNull(result.employees)
        assertEquals(3, result.employees?.size)
    }
}