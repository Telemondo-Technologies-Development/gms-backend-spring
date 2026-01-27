package com.gms.backend.domain.infra.quartz.asset

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class MaintenanceWorkerJob(
    private val service: MaintenanceSchedulerService
    ): QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val authorities = AuthorityUtils.createAuthorityList("maintenanceSchedule_update")
        val auth = UsernamePasswordAuthenticationToken("system_maintenanceSchedule_job", null, authorities)
        SecurityContextHolder.getContext().authentication = auth
        try {
            val data = context.jobDetail.jobDataMap
            service.executeWorkerTask(
                UUID.fromString(data.getString("scheduleId")),
                Instant.ofEpochMilli(data.getLong("targetDate")),
                data.getString("action")
            )
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}