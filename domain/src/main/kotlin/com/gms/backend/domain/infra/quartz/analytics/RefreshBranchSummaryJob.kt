package com.gms.backend.domain.infra.quartz.analytics

import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class RefreshBranchSummaryJob(
    private val jdbcTemplate: JdbcTemplate
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val authorities = AuthorityUtils.createAuthorityList()
        val auth = UsernamePasswordAuthenticationToken("system_branchSummaryRefresh_job", null, authorities)
        SecurityContextHolder.getContext().authentication = auth

        try {
            jdbcTemplate.execute("CALL refresh_branch_financial_summary()")
        } finally {
            SecurityContextHolder.clearContext()
        }
    }
}