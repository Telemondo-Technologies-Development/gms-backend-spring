package com.gms.backend.domain.application.mapper.expense

import com.gms.backend.domain.application.rest.expense.AssetExpenseController
import com.gms.backend.domain.domain.model.expense.AssetExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.UUID

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AssetExpenseMapper {
    @Mapping(target = "objectIds", source = "assetExpensesObjects")
    fun toReadDto(assetExpense: AssetExpense): AssetExpenseController.AssetExpenseReadDTO

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toAssetExpense(dto: AssetExpenseController.AssetExpenseCreateDTO): AssetExpense

    @Mapping(target = "updatedBy", ignore = true)
    fun updateFromDto(dto: AssetExpenseController.AssetExpenseUpdateDTO, @MappingTarget entity: AssetExpense): AssetExpense

    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}