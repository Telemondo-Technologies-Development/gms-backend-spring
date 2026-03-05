package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.BrandController
import com.gms.backend.domain.domain.model.asset.Brand
import com.gms.backend.domain.domain.model.asset.MaintenanceSchedule
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface BrandMapper {
    fun brandPostDTOToBrand(dto: BrandController.BrandPostDTO): Brand
    fun brandPutDTOToBrand(dto: BrandController.BrandPutDTO, @MappingTarget schedule: Brand): Brand
    fun brandToDTO(brand: Brand): BrandController.BrandTableDTO
}