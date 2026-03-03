package com.gms.backend.domain.infra.quartz.billing

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class InvoiceStatusJob(
    private val invoiceService: InvoiceScheduleService
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val authorities = AuthorityUtils.createAuthorityList("invoiceSchedule_update")
        val auth = UsernamePasswordAuthenticationToken("system_invoiceSchedule_update", null, authorities)
        SecurityContextHolder.getContext().authentication = auth
        try {
            invoiceService.checkPending()
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}