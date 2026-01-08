package com.gms.backend.domain.application.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {
    // TODO: Figure out if a custom error message is sent and the actual error is logged
    // TODO: Talk about how much data should be relayed to the user

    // Handle custom Domain errors
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponse.error(
            message = ex.message ?: "Action failed",
            status = HttpStatus.BAD_REQUEST,
            errors = ex.errors
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponse.error(
            ex.message ?: "Resource not found",
            HttpStatus.NOT_FOUND,
            listOf(ApiError("E0001", "NoSuchElementExeption"))
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Nothing>> {
        val name = ex.name
        val type = ex.requiredType?.simpleName
        val value = ex.value

        val errorDescription = "Parameter '$name' expected type '$type' but got '$value'"

        return ApiResponse.error(
            message = "Invalid request parameter",
            status = org.springframework.http.HttpStatus.BAD_REQUEST,
            errors = listOf(ApiError(code = "TypeMismatch", description = errorDescription))
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralError(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        println("Exception: ${ex}")
        return ApiResponse.error("An internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

class DomainException(
    message: String,
    val errors: List<ApiError>? = null
) : RuntimeException(message)