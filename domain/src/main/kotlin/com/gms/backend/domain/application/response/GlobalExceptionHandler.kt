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
    @ExceptionHandler(DomainExceptions::class)
    fun handleDomainExceptions(ex: DomainExceptions): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponse.error(
            message = ex.message ?: "Action failed",
            status = ex.status,
            errors = ex.errors
        )
    }

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponse.error(
            message = ex.message ?: "Action failed",
            status = ex.status,
            errors = listOf(ex.error.toApiError(ex.description))
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<ApiResponse<Nothing>> {
        return ApiResponse.error(
            message = ex.message ?: "Resource not found",
            status = HttpStatus.NOT_FOUND,
            errors = listOf(ApiErrorType.NO_SUCH_ELEMENT.toApiError())
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Nothing>> {
        val name = ex.name
        val type = ex.requiredType?.simpleName
        val value = ex.value

        val errorMessage = "Parameter '$name' expected type '$type' but got '$value'"

        return ApiResponse.error(
            message = errorMessage,
            status = HttpStatus.BAD_REQUEST,
            errors = listOf(ApiErrorType.DATATYPE_MISMATCH.toApiError())
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralError(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        println("Exception: $ex")
        return ApiResponse.error("An internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

// Check if status is necessary
class DomainExceptions(
    val errors: List<ApiError>? = null,
    message: String? = null,
    val status: HttpStatus = HttpStatus.BAD_REQUEST
) : RuntimeException(message)

class DomainException(
    val error: ApiErrorType,
    val description: String? = null,
    message: String? = null,
    val status: HttpStatus = HttpStatus.BAD_REQUEST
) : RuntimeException(message)