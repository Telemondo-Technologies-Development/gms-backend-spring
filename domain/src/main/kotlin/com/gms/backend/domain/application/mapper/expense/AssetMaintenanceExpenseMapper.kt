package com.gms.backend.domain.application.mapper.expense

import com.gms.backend.domain.application.rest.expense.AssetMaintenanceExpenseController
import com.gms.backend.domain.domain.model.expense.AssetMaintenanceExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.UUID

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetMaintenanceExpenseMapper {
    @Mapping(target = "objectIds", source = "assetMaintenanceExpensesObjects")
    fun toReadDto(expense: AssetMaintenanceExpense): AssetMaintenanceExpenseController.AssetMaintenanceExpenseReadDTO

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toAssetMaintenanceExpense(dto: AssetMaintenanceExpenseController.AssetMaintenanceExpenseCreateDTO): AssetMaintenanceExpense

    @Mapping(target = "updatedBy", ignore = true)
    fun updateFromDto(dto: AssetMaintenanceExpenseController.AssetMaintenanceExpenseUpdateDTO, @MappingTarget entity: AssetMaintenanceExpense): AssetMaintenanceExpense

    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}