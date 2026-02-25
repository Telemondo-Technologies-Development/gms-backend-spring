package com.gms.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class GmsBackendSpringApplication

fun main(args: Array<String>) {
	runApplication<GmsBackendSpringApplication>(*args)
}
