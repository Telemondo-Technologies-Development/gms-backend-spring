package com.gms.backend.domain.infra.quartz.asset

import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class MaintenanceJob(
    private val schedulerService: MaintenanceSchedulerService
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        // Calculate next due date and create new maintenance records
        schedulerService.processSchedules()

        // change the status from pending to overdue if the due date is passed
        schedulerService.updateOverdueStatuses()
    }
}