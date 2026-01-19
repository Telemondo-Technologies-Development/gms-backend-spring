package com.gms.backend.domain.application.response

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {
    // TODO: Figure out if a custom error message is sent and the actual error is logged
    // TODO: Talk about how much data should be relayed to the user

    companion object {
        // Defines the logger for this class
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    // Handle custom Domain errors
    @ExceptionHandler(DomainExceptions::class)
    fun handleDomainExceptions(ex: DomainExceptions): ResponseEntity<ApiResponse<Nothing>> {
//        val codes = ex.errors?.map { it.code } ?: emptyList()
        log.warn("Domain validation failed: {} | Errors: {}", ex.message, ex.errors)

        return ApiResponse.error(
            message = ex.message ?: "Action failed",
            status = ex.status,
            errors = ex.errors
        )
    }

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Domain error: {} - {}", ex.error, ex.description)

        return ApiResponse.error(
            message = ex.message ?: "Action failed",
            status = ex.status,
            errors = listOf(ex.error.toApiError(ex.description))
        )
    }

    // Handle Unauthenticated (401)
    // Occurs when user is not logged in or token is missing/invalid
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Authentication failed: {}", ex.message)
        return ApiResponse.error(
            message = "Authentication required. Please log in.",
            status = HttpStatus.UNAUTHORIZED,
            errors = listOf(ApiErrorType.UNAUTHENTICATED.toApiError(ex.message))
        )
    }

    // Handle Unauthorized (403)
    // Occurs when @PreAuthorize check fails
    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAccessDeniedException(ex: AuthorizationDeniedException): ResponseEntity<ApiResponse<Nothing>> {
        log.warn("Access denied: {}", ex.message)
        return ApiResponse.error(
            message = "You do not have permission to perform this action.",
            status = HttpStatus.FORBIDDEN,
            errors = listOf(ApiErrorType.INSUFFICIENT_PERMISSIONS.toApiError(ex.message))
        )
    }

    // Handle validation errors for @RequestBody (e.g. JSON input)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errors = ex.bindingResult.fieldErrors.map { fieldError ->
            ApiErrorType.INVALID_CASE.toApiError(
                fieldError.defaultMessage ?: "Invalid value",
                fieldError.field
            )
        }

        log.warn("Validation failed for request: {}", errors)

        return ApiResponse.error(
            message = "Validation failed",
            status = HttpStatus.BAD_REQUEST,
            errors = errors
        )
    }

    // 2. Handle validation errors at the Entity/JPA level
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ApiResponse<Nothing>> {
        val errors = ex.constraintViolations.map { violation ->
            ApiErrorType.INVALID_CASE.toApiError(
                violation.message,
                violation.propertyPath.toString()
            )
        }

        log.warn("JPA constraint violation: {}", errors)

        return ApiResponse.error(
            message = "Database validation failed",
            status = HttpStatus.BAD_REQUEST,
            errors = errors
        )
    }

    // Handle MinIO Server Errors
//    @ExceptionHandler(ErrorResponseException::class)
//    fun handleMinioStorageError(ex: ErrorResponseException): ResponseEntity<ApiResponse<Nothing>> {
//        val errorCode = ex.errorResponse().code()
//        log.warn("MinIO Storage Error: {} | Bucket: {} | Object: {}",
//            errorCode, ex.errorResponse().bucketName(), ex.errorResponse().objectName())
//
//        val (status, apiError) = when (errorCode) {
//            "NoSuchKey", "NoSuchBucket" ->
//                HttpStatus.NOT_FOUND to ApiErrorType.NO_SUCH_ELEMENT
//            "AccessDenied" ->
//                HttpStatus.FORBIDDEN to ApiErrorType.INSUFFICIENT_PERMISSIONS
//            else ->
//                HttpStatus.INTERNAL_SERVER_ERROR to ApiErrorType.INTERNAL_SERVER_ERROR
//        }
//
//        return ApiResponse.error(
//            message = "Storage action failed: $errorCode",
//            status = status,
//            errors = listOf(apiError.toApiError("MinIO code: $errorCode"))
//        )
//    }

    // Handle MinIO Client Errors
//    @ExceptionHandler(MinioException::class)
//    fun handleGenericMinioError(ex: MinioException): ResponseEntity<ApiResponse<Nothing>> {
//        log.error("MinIO SDK failure: ", ex)
//        return ApiResponse.error(
//            message = "A storage service error occurred.",
//            status = HttpStatus.BAD_GATEWAY,
//            errors = listOf(ApiErrorType.INTERNAL_ERROR.toApiError(ex.message))
//        )
//    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<ApiResponse<Nothing>> {
        log.info("Resource not found: {}", ex.message)

        return ApiResponse.error(
            message = ex.message ?: "Resource not found",
            status = HttpStatus.NOT_FOUND,
            errors = listOf(ApiErrorType.NO_SUCH_ELEMENT.toApiError())
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Nothing>> {
        val errorMessage = "Parameter '${ex.name}' expected type '${ex.requiredType?.simpleName}' but got '${ex.value}'"
        log.debug("Type mismatch error: {}", errorMessage)

        return ApiResponse.error(
            message = errorMessage,
            status = HttpStatus.BAD_REQUEST,
            errors = listOf(ApiErrorType.DATATYPE_MISMATCH.toApiError())
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralError(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        log.error("Unhandled exception occurred: ", ex)
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