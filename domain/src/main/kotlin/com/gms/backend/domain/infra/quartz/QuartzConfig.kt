package com.gms.backend.domain.infra.quartz
import com.gms.backend.domain.infra.quartz.asset.MaintenanceJob
import org.quartz.*
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Configuration
class QuartzConfig {

    // For maintenance schedule job
    @Bean
    fun maintenanceJobDetail(): JobDetail = JobBuilder.newJob(MaintenanceJob::class.java)
        .withIdentity("maintenanceJob")
        .storeDurably()
        .build()

    @Bean
    fun maintenanceJobTrigger(): Trigger = TriggerBuilder.newTrigger()
        .forJob(maintenanceJobDetail())
        .withIdentity("maintenanceTrigger")
        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")) //every 12 AM
        .build()
}

// main job runs everytime the app starts
@Component
class MaintenanceStartupRunner(private val scheduler: Scheduler) {
    @EventListener(ApplicationReadyEvent::class)
    fun runJobOnStartup() {
        val jobKey = JobKey("maintenanceJob")
        scheduler.triggerJob(jobKey)
    }
}