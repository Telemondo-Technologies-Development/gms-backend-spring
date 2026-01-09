package com.gms.backend.domain.application.response

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T? = null,
    val errors: List<ApiError>? = null,
    val meta: PageMetadata? = null,
    val timestamp: Long = Instant.now().epochSecond
) {
    companion object {
        fun <T> ok(data: T, message: String? = "Success"): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.ok(ApiResponse(true, message, data))

        fun <T> created(data: T, message: String? = "Created Successfully"): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse(true, message, data))

        fun error(
            message: String? = "Error",
            status: HttpStatus,
            errors: List<ApiError>? = null
        ): ResponseEntity<ApiResponse<Nothing>> {
            return ResponseEntity.status(status).body(ApiResponse(false, message, errors = errors))
        }

        // This method maps Spring's Page object to our ApiResponse
        // Unverified
        fun <T: Any> paginated(page: Page<T>, message: String): ResponseEntity<ApiResponse<List<T>>> {
            val metadata = PageMetadata(
                pageIndex = page.number,
                pageSize = page.size,
                pageCount = page.totalPages,
                totalCount = page.totalElements
            )
            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = message,
                    data = page.content,
                    meta = metadata
                )
            )
        }
    }
}

// For standard 200 OK responses
fun <T> T.toOkResponse(message: String? = "Ok"): ResponseEntity<ApiResponse<T>> {
    return ApiResponse.ok(this, message)
}

// For 201 Created responses
fun <T> T.toCreatedResponse(message: String? = "Created"): ResponseEntity<ApiResponse<T>> {
    return ApiResponse.created(this, message)
}

data class ApiError(
    val code: String,
    val description: String
)

// Think of a way to categorize error code
enum class ApiErrorType(val code: String, val description: String) {
    // 400 Bad Request / Validation Errors
    INVALID_ID("VAL_001", "The provided ID is not in a valid for or does not exist."),
    DATATYPE_MISMATCH("VAL_002", "The data type provided does not match the expected format for this field."),
    MALFORMED_JSON("VAL_003", "The request body contains invalid JSON syntax."),

    // Create
    NULL_UPDATE("VAL_004", "The updated failed as the data provided does not exist."),


    // Delete
    NO_DELETE("VAL_005", "No entry was sent to be deleted."),
    NULL_DELETE("VAL_006", "The delete failed as the data provided does not exist."),

    // 404 Not Found Errors
    RESOURCE_NOT_FOUND("VAL_401", "The requested resource could not be located."),
    NO_SUCH_ELEMENT("VAL_402", "The specific item you are looking for is missing or has been deleted."),

    // 500 Internal Errors
    INTERNAL_SERVER_ERROR("VAL_501", "An unexpected error occurred on our end. Please try again later.");

    fun toApiError(override: String? = null) = ApiError(
        code = this.code,
        description = override ?: this.description
    )
}

data class PageMetadata(
    val pageIndex: Int,       // TanStack uses 0-based index by default
    val pageSize: Int,        // Current size of the request
    val pageCount: Int,       // Total number of pages
    val totalCount: Long      // Total number of records (used for TanStack Table's rowCount)
)

