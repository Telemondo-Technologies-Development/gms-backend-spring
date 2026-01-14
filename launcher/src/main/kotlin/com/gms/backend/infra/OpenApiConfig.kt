package com.gms.backend.infra

import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig {

    @Bean
    fun schemaPrefixFilter(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            val prefixesToOmit = listOf("Deprecated") // "ApiResponse",
            openApi.components?.schemas?.let { schemas ->
                schemas.keys.removeIf { schemaName ->
                    prefixesToOmit.any { prefix -> schemaName.startsWith(prefix) && schemaName != prefix }
                }
            }
        }
    }
}