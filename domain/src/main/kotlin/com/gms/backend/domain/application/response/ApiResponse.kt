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

data class PageMetadata(
    val pageIndex: Int,       // TanStack uses 0-based index by default
    val pageSize: Int,        // Current size of the request
    val pageCount: Int,       // Total number of pages
    val totalCount: Long      // Total number of records (used for TanStack Table's rowCount)
)

