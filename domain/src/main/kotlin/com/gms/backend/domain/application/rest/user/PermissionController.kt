package com.gms.backend.domain.application.rest.user

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.impl.domain.service.permission.PermissionServiceImpl
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/permission")
class PermissionController(private val permissionService: PermissionServiceImpl) {

    @GetMapping
    fun getAllPermissions() = permissionService.getPermissions().toOkResponse()

    @GetMapping("/{id}")
    fun getPermission(@PathVariable id: UUID) =
        permissionService.getPermissionById(id).toOkResponse()

    // TODO: Remove Create for Production
    @PostMapping
    fun createPermission(@RequestBody body: MutableSet<String>) =
        permissionService.createPermission(body).toCreatedResponse()
}