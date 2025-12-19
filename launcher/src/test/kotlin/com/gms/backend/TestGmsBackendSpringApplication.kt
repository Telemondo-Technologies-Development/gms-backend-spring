package com.gms.backend

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<GmsBackendSpringApplication>().with(TestcontainersConfiguration::class).run(*args)
}
