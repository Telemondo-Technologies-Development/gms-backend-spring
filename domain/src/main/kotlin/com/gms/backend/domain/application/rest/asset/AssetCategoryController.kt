package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.domain.service.asset.AssetCategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/asset/category")
@Tag(name = "Asset Category")
class AssetCategoryController (
    private val assetCategoryService: AssetCategoryService
){
    @Schema(description = "Format for Asset Category read")
    data class AssetCategoryTableDTO(
        val id: UUID,
        val name: String,
        val createdById: UUID?,
        val updatedById: UUID?,
//        val createdAt: Instant,
//        val updatedAt: Instant
    )

    @Schema(description = "Format for Asset Category create")
    data class AssetCategoryPostDTO(
        @field: NotBlank(message = "Category name is required")
        val name: String,
        val createdById: UUID
    )

    @Schema(description = "Format for Asset Category update")
    data class AssetCategoryPutDTO(
        @field:NotBlank(message = "Category name is required")
        val name: String,
        val updatedById: UUID
    )

    @PostMapping
    @Operation(summary = "Create a new Asset Category")
    fun createAssetCategory(@Valid @RequestBody body: AssetCategoryPostDTO) =
        assetCategoryService.createAssetCategory(body).toCreatedResponse("Asset Category Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Asset Category by id")
    fun updateAssetCategory(@PathVariable id: UUID, @Valid @RequestBody body: AssetCategoryPutDTO) =
        assetCategoryService.updateAssetCategory(id, body).toOkResponse("Asset Category Updated")

    @GetMapping
    @Operation(summary = "Get all Asset Categories")
    fun getAllAssetCategories() =
        assetCategoryService.getAssetCategories().toOkResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Asset Category by id")
    fun getAssetCategory (@PathVariable id: UUID) =
        assetCategoryService.getAssetCategoryById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Asset Category by id")
    fun deleteAssetCategory(@PathVariable id: UUID) =
        assetCategoryService.deleteAssetCategory(id).toOkResponse("Asset Category Deleted")

}