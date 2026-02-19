package com.gms.backend.domain.application.mapper.expense

import com.gms.backend.domain.application.rest.expense.UtilityExpenseController
import com.gms.backend.domain.domain.model.expense.UtilityExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.UUID

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UtilityExpenseMapper {
    @Mapping(target = "objectIds", source = "utilityExpensesObjects")
    fun toReadDto(utilityExpense: UtilityExpense): UtilityExpenseController.UtilityExpenseReadDTO

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toUtilityExpense(dto: UtilityExpenseController.UtilityExpenseCreateDTO): UtilityExpense

    @Mapping(target = "updatedBy", ignore = true)
    fun updateUtilityExpenseFromDto(dto: UtilityExpenseController.UtilityExpenseUpdateDTO, @MappingTarget entity: UtilityExpense): UtilityExpense

    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}