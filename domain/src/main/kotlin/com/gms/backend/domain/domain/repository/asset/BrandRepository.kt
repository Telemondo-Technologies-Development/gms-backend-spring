package com.gms.backend.domain.domain.repository.asset

import com.gms.backend.domain.application.rest.asset.BrandController
import com.gms.backend.domain.domain.model.asset.Brand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BrandRepository : JpaRepository<Brand, UUID>{
    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.asset.BrandController$BrandTableDTO(
                b.id,
                b.name,
                b.createdById,
                b.updatedById,
                b.createdAt,
                b.updatedAt
            )
            FROM Brand b
        """, countQuery = "SELECT COUNT(b) FROM Brand b"
    )
    fun findAllProjectedBy(pageable: Pageable): Page<BrandController.BrandTableDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.asset.BrandController$BrandMappingDTO(b.id, o.id)
                FROM Brand b
                JOIN b.brandObjects o 
                WHERE b.id IN :brandIds
        """)
    fun findAllObjectIdsByBrandIds(@Param("brandIds") brandIds: List<UUID>): List<BrandController.BrandMappingDTO>

    @Query(
        value = $$"""
        SELECT new com.gms.backend.domain.application.rest.asset.BrandController$BrandTableDTO(
                b.id,
                b.name,
                b.createdById,
                b.updatedById,
                b.createdAt,
                b.updatedAt
            )
            FROM Brand b
            WHERE b.id = :id
        """)
    fun findProjectedBy(@Param("id") id: UUID): Optional<BrandController.BrandTableDTO>

    @Query("""
        SELECT o.id 
            FROM Brand b 
            JOIN b.brandObjects o 
            WHERE b.id = :brandId
        """)
    fun findObjectIdsByBrandId(@Param("brandId") brandId: UUID): List<UUID>

}
