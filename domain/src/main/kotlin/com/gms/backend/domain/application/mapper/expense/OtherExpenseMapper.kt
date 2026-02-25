package com.gms.backend.domain.application.mapper.expense

import com.gms.backend.domain.application.rest.expense.OtherExpenseController
import com.gms.backend.domain.domain.model.expense.OtherExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.*

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface OtherExpenseMapper {
    @Mapping(target = "objectIds", source = "otherExpensesObjects")
    fun toReadDto(otherExpense: OtherExpense): OtherExpenseController.OtherExpenseReadDTO

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toOtherExpense(dto: OtherExpenseController.OtherExpenseCreateDTO): OtherExpense

    @Mapping(target = "updatedBy", ignore = true)
    fun updateOtherExpenseFromDto(dto: OtherExpenseController.OtherExpenseUpdateDTO, @MappingTarget entity: OtherExpense): OtherExpense

    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}