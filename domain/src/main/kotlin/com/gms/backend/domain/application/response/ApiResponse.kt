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
        fun <T: Any> paginated(page: Page<T>, message: String? = "Success"): ResponseEntity<ApiResponse<List<T>>> {
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

fun <T: Any> Page<T>.toPaginatedResponse(message: String? = "Ok"): ResponseEntity<ApiResponse<List<T>>> {
    return ApiResponse.paginated(this, message)
}

data class ApiError(
    val code: String,
    val description: String,
    val field: String? = null
)

// Think of a way to categorize error code
enum class ApiErrorType(val code: String, val description: String) {
    // 400 Bad Request / Validation Errors
    INVALID_ID("VAL_001", "The provided ID is not in a valid for or does not exist."),
    DATATYPE_MISMATCH("VAL_002", "The data type provided does not match the expected format for this field."),
    MALFORMED_JSON("VAL_003", "The request body contains invalid JSON syntax."),
    INVALID_CASE("VAL_007", "The data provided does not meet validation requirements"),
    RESOURCE_ALREADY_EXISTS("VAL_008", "A resource with the same unique identifiers already exists."),
    DUPLICATE_ENTRY("VAL_009", "The action resulted in a duplicate entry that violates database constraints."),
    CONCURRENT_MODIFICATION("VAL_010", "The resource was modified by another process. Please refresh and try again."),
    FK_VIOLATION("VAL_011", "This action is restricted because this record is linked to other data."),

    // Create
    MISSING_UPDATE("VAL_004", "The updated failed as some of the data provided do not exist."),

    // Delete
    NO_MODIFICATION("VAL_005", "No entry was sent to be modified."),
    MISSING_DELETE("VAL_006", "The delete failed as some of the data provided does not exist."),

    // 401 Unauthorized (Authentication)
    UNAUTHENTICATED("SEC_001", "Full authentication is required to access this resource."),

    // 403 Forbidden (Authorization)
    INSUFFICIENT_PERMISSIONS("SEC_002", "You do not have the required authorities to perform this action."),

    // 404 Not Found Errors
    RESOURCE_NOT_FOUND("VAL_401", "The requested resource could not be located."),
    NO_SUCH_ELEMENT("VAL_402", "The specific item you are looking for is missing or has been deleted."),

    // 500 Internal Errors
    INTERNAL_SERVER_ERROR("VAL_501", "An unexpected error occurred on our end. Please try again later.");

    fun toApiError(description: String? = null, field: String? = null) = ApiError(
        code = this.code,
        description = description ?: this.description,
        field = field
    )
}

data class PageMetadata(
    val pageIndex: Int,       // TanStack uses 0-based index by default
    val pageSize: Int,        // Current size of the request
    val pageCount: Int,       // Total number of pages
    val totalCount: Long      // Total number of records (used for TanStack Table's rowCount)
)

