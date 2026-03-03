package com.gms.backend.domain.impl.domain.service.billing

import com.gms.backend.domain.application.mapper.billing.PaymentMapper
import com.gms.backend.domain.application.rest.billing.PaymentController
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.model.billing.Payment
import com.gms.backend.domain.domain.repository.billing.InvoiceRepository
import com.gms.backend.domain.domain.repository.billing.PaymentMethodRepository
import com.gms.backend.domain.domain.repository.billing.PaymentRepository
import com.gms.backend.domain.domain.repository.user.ActorRepository
import com.gms.backend.domain.domain.service.billing.PaymentService
import com.gms.backend.domain.infra.quartz.billing.InvoiceScheduleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@PreAuthorize("denyAll()")
class PaymentServiceImpl(
    private val paymentRepository: PaymentRepository,
    private val paymentMapper: PaymentMapper,
    private val actorRepository: ActorRepository,
    private val invoiceRepository: InvoiceRepository,
    private val invoiceScheduleService: InvoiceScheduleService,
    private val paymentMethodRepository: PaymentMethodRepository
) : PaymentService {
    @Transactional
    @PreAuthorize("hasAuthority('payment_create')")
    override fun createPayment(body: PaymentController.PaymentPostDTO): PaymentController.PaymentTableDTO {
        val payment = paymentMapper.paymentPostDTOToPayment(body).apply {
            invoice = invoiceRepository.getReferenceById(body.invoiceId)
            paymentMethod = paymentMethodRepository.getReferenceById(body.paymentMethodId)
            createdBy = actorRepository.getReferenceById(body.createdById)
            updatedBy = actorRepository.getReferenceById(body.createdById)
        }

        val saved = paymentRepository.saveAndFlush(payment)
        val invoice = invoiceRepository.findById(body.invoiceId).orElseThrow()
        when (saved.status) {
            Payment.PaymentStatus.FULL -> {
                invoice.apply { status = Invoice.InvoiceStatus.PAID }
                invoiceRepository.save(invoice)
                invoiceScheduleService.createPendingInvoice(invoice.id)
            }
            Payment.PaymentStatus.PARTIAL -> {
                invoice.apply { status = Invoice.InvoiceStatus.PARTIAL }
                invoiceRepository.save(invoice)
            }
            else -> {
                // Optional: handle other statuses (e.g., PENDING, FAILED)
            }
        }

        return paymentMapper.paymentToPaymentTableDTO(saved)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('payment_read')")
    override fun getPayments(pageable: Pageable): Page<PaymentController.PaymentTableDTO> {
        return paymentRepository.findAllProjectedBy(pageable)
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('payment_read')")
    override fun getPaymentById(id: UUID): PaymentController.PaymentTableDTO {
        val entity = paymentRepository.findById(id).orElseThrow()
        return paymentMapper.paymentToPaymentTableDTO(entity)
    }

    @Transactional
    @PreAuthorize("hasAuthority('payment_update')")
    override fun updatePayment(
        id: UUID,
        body: PaymentController.PaymentPutDTO
    ): PaymentController.PaymentTableDTO {
        val payment = paymentRepository.findById(id).orElseThrow()
        payment.apply {
            paymentMapper.paymentPutDTOToPayment(body, this)
            paymentMethod = paymentMethodRepository.getReferenceById(body.paymentMethodId)
            updatedBy = actorRepository.getReferenceById(body.updatedById)
        }

        paymentRepository.saveAndFlush(payment)
        return paymentMapper.paymentToPaymentTableDTO(payment)
    }

    @Transactional
    @PreAuthorize("hasAuthority('payment_delete')")
    override fun deletePayment(id: UUID) {
        val payment = paymentRepository.findById(id).orElseThrow()
        paymentRepository.delete(payment)
    }

}