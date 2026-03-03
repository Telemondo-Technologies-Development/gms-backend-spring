package com.gms.backend.domain.infra.quartz.billing

import com.gms.backend.domain.application.response.ApiErrorType
import com.gms.backend.domain.application.response.DomainException
import com.gms.backend.domain.application.rest.billing.InvoiceController
import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.repository.billing.InvoiceRepository
import com.gms.backend.domain.domain.service.billing.InvoiceService
import org.quartz.JobExecutionContext
import org.springframework.core.env.Environment
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Instant
import java.util.*

class InvoiceRenewalJob(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceService: InvoiceService,
    private val environment: Environment
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val authorities = AuthorityUtils.createAuthorityList( "invoice_create")
        val auth = UsernamePasswordAuthenticationToken("system_invoiceSchedule_create", null, authorities)
        SecurityContextHolder.getContext().authentication = auth
        try {
            val systemId = UUID.fromString(environment.getProperty("system.id"))
                ?: throw DomainException(ApiErrorType.INTERNAL_SERVER_ERROR, "System ID configuration missing")
            val invoiceId = context.jobDetail.jobDataMap.getString("invoiceId")
            val nextDueDate = context.jobDetail.jobDataMap.getString("nextDueDate")
            val invoice = invoiceRepository.findById(UUID.fromString(invoiceId)).orElseThrow()
            val newInvoice = InvoiceController.InvoicePostDTO(
                actorId = invoice.actorId!!,
                memberSubscriptionId = invoice.memberSubscriptionId!!,
                dueDate = Instant.parse(nextDueDate),
                status = Invoice.InvoiceStatus.PENDING,
                systemGenerated = true,
                createdById = systemId
            )

            invoiceService.createInvoice(newInvoice)
        } finally {
            SecurityContextHolder.clearContext()
        }

    }
}