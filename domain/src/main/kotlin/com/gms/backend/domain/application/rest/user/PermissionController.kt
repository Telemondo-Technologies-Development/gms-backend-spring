package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.impl.domain.service.user.PermissionServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/permission")
@Tag(name = "Access Control")
class PermissionController(private val permissionService: PermissionServiceImpl) {

    @GetMapping
    @Operation(summary = "Get all Permissions")
    fun getAllPermissions() = permissionService.getPermissions().toOkResponse()

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