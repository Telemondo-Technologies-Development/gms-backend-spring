package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.user.PermissionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/permission")
@Tag(name = "Access Control")
class PermissionController(private val permissionService: PermissionService) {

    @GetMapping
    @Operation(summary = "Get all Permissions")
    fun getAllPermissions(pageable: Pageable) = permissionService.getPermissions(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Permission by id")
    fun getPermission(@PathVariable id: UUID) =
        permissionService.getPermissionById(id).toOkResponse()

    // TODO: Remove Create for Production
    @PostMapping
    @Operation(summary = "[DEV] Create a new Permission")
    fun createPermission(@RequestBody body: MutableSet<String>) =
        permissionService.createPermission(body).toCreatedResponse()
}