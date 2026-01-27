package com.gms.backend.domain.impl.domain.service.billing

import com.gms.backend.domain.application.mapper.billing.InvoiceMapper
import com.gms.backend.domain.application.rest.billing.InvoiceController
import com.gms.backend.domain.domain.repository.billing.InvoiceRepository
import com.gms.backend.domain.domain.repository.branch.BranchRepository
import com.gms.backend.domain.domain.repository.member.MemberSubscriptionRepository
import com.gms.backend.domain.domain.repository.subscription.SubscriptionAvailedRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.billing.InvoiceService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@PreAuthorize("denyAll()")
class InvoiceServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceMapper: InvoiceMapper,
    private val actorRepository: ActorRepository,
    private val branchRepository: BranchRepository,
    private val memberSubscriptionRepository: MemberSubscriptionRepository,
    private val subscriptionAvailedRepository: SubscriptionAvailedRepository,
) : InvoiceService {
    @Transactional
    @PreAuthorize("hasAuthority('invoice_create')")
    override fun createInvoice(body: InvoiceController.InvoicePostDTO): InvoiceController.InvoiceTableDTO {
        val invoice = invoiceMapper.invoicePostDTOToInvoice(body).apply {
            actor = actorRepository.getReferenceById(body.actorId)
            branch = branchRepository.getReferenceById(body.branchId)
            subscriptionAvailed = subscriptionAvailedRepository.getReferenceById(body.subscriptionAvailedId)
            memberSubscription = memberSubscriptionRepository.getReferenceById(body.memberSubscriptionId)
            issuedAt = Instant.now()
            // Currently has no discount
            total = body.subtotal
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = invoiceRepository.saveAndFlush(invoice)
        return invoiceMapper.invoiceToInvoiceTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('invoice_read')")
    override fun getInvoices(pageable: Pageable): Page<InvoiceController.InvoiceTableDTO> {
        return invoiceRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('invoice_read')")
    override fun getInvoiceById(id: UUID): InvoiceController.InvoiceTableDTO {
        val entity = invoiceRepository.findById(id).orElseThrow()
        return invoiceMapper.invoiceToInvoiceTableDTO(entity)
    }

    @Transactional
    @PreAuthorize("hasAuthority('invoice_update')")
    override fun updateInvoice(
        id: UUID,
        body: InvoiceController.InvoicePutDTO
    ): InvoiceController.InvoiceTableDTO {
        val invoice = invoiceRepository.findById(id).orElseThrow()
        invoice.apply {
            invoiceMapper.invoicePutDTOToInvoice(body, this)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        invoiceRepository.saveAndFlush(invoice)
        return invoiceMapper.invoiceToInvoiceTableDTO(invoice)
    }

    @Transactional
    @PreAuthorize("hasAuthority('invoice_delete')")
    override fun deleteInvoice(id: UUID) {
        val invoice = invoiceRepository.findById(id).orElseThrow()
        invoiceRepository.delete(invoice)
    }

}