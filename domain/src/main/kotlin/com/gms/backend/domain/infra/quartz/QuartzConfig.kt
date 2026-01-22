package com.gms.backend.domain.infra.quartz
import com.gms.backend.domain.infra.quartz.asset.MaintenanceJob
import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
        .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")) // will trigger every minute
        .build()
}