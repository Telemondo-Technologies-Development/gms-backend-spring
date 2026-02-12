package com.gms.backend.domain.infra.quartz.asset

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class MaintenanceJob(
    private val schedulerService: MaintenanceSchedulerService
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        println("DEBUG: MaintenanceJob is running now ${java.time.Instant.now()}")
        val authorities = AuthorityUtils.createAuthorityList("maintenanceSchedule_update")
        val auth = UsernamePasswordAuthenticationToken("system_maintenanceSchedule_job", null, authorities)
        SecurityContextHolder.getContext().authentication = auth

        try {
            schedulerService.processSchedules()
//            schedulerService.updateOverdueStatuses()
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}