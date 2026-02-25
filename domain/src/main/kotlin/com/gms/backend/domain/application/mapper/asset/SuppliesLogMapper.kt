package com.gms.backend.domain.application.mapper.asset

import com.gms.backend.domain.application.rest.asset.SuppliesLogController
import com.gms.backend.domain.domain.model.asset.SuppliesLog
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.*

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SuppliesLogMapper {
    fun suppliesLogPostDTOToSuppliesLog(dto: SuppliesLogController.SuppliesLogPostDTO): SuppliesLog
    fun suppliesLogPutDTOToSuppliesLog(
        dto: SuppliesLogController.SuppliesLogPutDTO,
        @MappingTarget suppliesLog: SuppliesLog
    ): SuppliesLog
    @Mapping(target = "objectIds", source = "suppliesLogObjects")
    fun suppliesLogToDTO(suppliesLog: SuppliesLog): SuppliesLogController.SuppliesLogTableDTO
    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}