package com.gms.backend.impl.service.billing

import com.gms.backend.domain.application.rest.billing.InvoiceController
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.repository.billing.InvoiceRepository
import com.gms.backend.domain.impl.domain.service.billing.InvoiceServiceImpl
import com.gms.backend.impl.service.BaseTest
import io.nats.client.Connection
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

class InvoiceServiceImplTest
@Autowired constructor(
    private val invoiceServiceImpl: InvoiceServiceImpl,
    private val invoiceRepository: InvoiceRepository,
    private val entityManager: EntityManager,
    private val nc: Connection
) : BaseTest() {

    @Test
    fun natsConnection() {
        assertEquals(nc.status, Connection.Status.CONNECTED)
    }

    @Test
    fun testReadInvoices() {
        // Given
        // When
        val invoices = invoiceServiceImpl.getInvoices(Pageable.unpaged())
        // Then
        assertEquals(2, invoices.size)
    }

    @Test
    fun testCreateInvoice() {
        // Given
        val createdById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val memberId = UUID.fromString("8140f50d-a33f-4569-b76a-20c348b77222")
        val memberSubscriptionId = UUID.fromString("019bd47a-eacd-7d47-89af-e3f9285da33a")
        val invoice = InvoiceController.InvoicePostDTO(
            actorId = memberId,
            memberSubscriptionId = memberSubscriptionId,
            subtotal = BigDecimal(1222),
            dueDate = Instant.now(),
            gracePeriodDate = Instant.now(),
            status = Invoice.InvoiceStatus.DRAFT,
            systemGenerated = true,
            createdById = createdById
        )
        // When
        val saved = invoiceServiceImpl.createInvoice(invoice)
        // Then
        assertEquals(0, saved.subtotal.compareTo(BigDecimal(1222)))
        assertEquals(createdById, saved.createdById)
    }

    @Test
    fun testUpdateInvoices() {
        // Given
        val updatedById = UUID.fromString("16868948-705e-4011-9f42-bcc324c01ce0")
        val invoice = InvoiceController.InvoicePutDTO(
            subtotal = BigDecimal(1222),
            dueDate = Instant.now(),
            gracePeriodDate = Instant.now(),
            status = Invoice.InvoiceStatus.DRAFT,
            updatedById = updatedById
        )
        val id = UUID.fromString("019c716e-86d4-7aeb-a8d2-517c7982a8d0")
        // When
        val updated = invoiceServiceImpl.updateInvoice(id, invoice)
        // Then
        assertEquals(0, updated.subtotal.compareTo(BigDecimal(1222)))
        assertEquals(updatedById, updated.updatedById)
    }

    @Test
    fun testDeleteInvoice() {
        // Then
        val id = UUID.fromString("019c716e-86d4-7aeb-a8d2-517c7982a8d0")
        val count = invoiceRepository.count()
        // When
        val invoice = invoiceServiceImpl.deleteInvoice(id)
        // Then
        assertEquals(count - 1, invoiceRepository.count())
        assertEquals(null, invoiceRepository.findById(id).getOrNull())
        assertThrows<NoSuchElementException> {
            invoiceServiceImpl.deleteInvoice(id)
        }
    }
}