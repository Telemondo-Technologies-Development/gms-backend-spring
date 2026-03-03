package com.gms.backend.domain.infra.quartz.billing

import com.gms.backend.domain.domain.model.billing.Invoice
import com.gms.backend.domain.domain.repository.billing.InvoiceRepository
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class SingleInvoiceStatusJob(
    private val invoiceService: InvoiceScheduleService,
    private val invoiceRepository: InvoiceRepository
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val authorities = AuthorityUtils.createAuthorityList("invoice_read", "invoice_update", "invoiceSchedule_update")
        val auth = UsernamePasswordAuthenticationToken("system_invoiceSchedule_update", null, authorities)
        SecurityContextHolder.getContext().authentication = auth
        try {
            val invoiceId = context.jobDetail.jobDataMap.getString("invoiceId")

            val invoice = invoiceRepository.findById(UUID.fromString(invoiceId)).orElseThrow().apply {
                status = Invoice.InvoiceStatus.DUE
            }
            val saved = invoiceRepository.save(invoice)

            invoiceService.overdue(saved)
        } finally {
            SecurityContextHolder.clearContext()
        }

    }
}