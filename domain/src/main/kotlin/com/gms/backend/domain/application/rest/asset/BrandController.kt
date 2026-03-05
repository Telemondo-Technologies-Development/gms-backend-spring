package com.gms.backend.domain.application.rest.asset

import com.gms.backend.domain.application.response.toCreatedResponse
import com.gms.backend.domain.application.response.toOkResponse
import com.gms.backend.domain.application.response.toPaginatedResponse
import com.gms.backend.domain.domain.service.asset.BrandService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/asset/brand")
@Tag(name = "Brand")
class BrandController (
    private val brandService: BrandService
){
    @Schema(description = "Format for Brand read")
    data class BrandTableDTO(
        val id: UUID,
        val name: String,
        val createdById: UUID?,
        val updatedById: UUID?,
        val createdAt: Instant,
        val updatedAt: Instant
    ){
        var objectIds: List<UUID> = emptyList()
    }

    data class BrandMappingDTO(
        val brandId: UUID,
        val relatedId: UUID,
    )

    @Schema(description = "Format for Brand create")
    data class BrandPostDTO(
        @field: NotBlank(message = "Name must not be empty")
        val name: String,
        val objectIds: List<UUID> = emptyList(),
        val createdById: UUID
    )

    @Schema(description = "Format for Brand update")
    data class BrandPutDTO(
        @field:NotBlank(message = "Name must not be empty")
        val name: String,
        val objectIds: List<UUID> = emptyList(),
        val updatedById: UUID
    )

    // Basic CRUD
    @PostMapping
    @Operation(summary = "Create a Brand Category")
    fun createBrand(@Valid @RequestBody body: BrandPostDTO) =
        brandService.createBrand(body).toCreatedResponse("Brand Created")

    @PutMapping("/{id}")
    @Operation(summary = "Update a Brand by id")
    fun updateBrand(@PathVariable id: UUID, @Valid @RequestBody body: BrandPutDTO) =
        brandService.updateBrand(id, body).toOkResponse("Brand Updated")

    @GetMapping
    @Operation(summary = "Get all Brands")
    fun getAllBrands(pageable: Pageable) =
        brandService.getBrands(pageable).toPaginatedResponse()

    @GetMapping("/{id}")
    @Operation(summary = "Get a Brand by id")
    fun getBrand (@PathVariable id: UUID) =
        brandService.getBrandById(id).toOkResponse()

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Brand by id")
    fun deleteBrand(@PathVariable id: UUID) =
        brandService.deleteBrand(id).toOkResponse("Brand Deleted")

}