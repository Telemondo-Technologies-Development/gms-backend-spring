package com.gms.backend.domain.application.mapper.expense

import com.gms.backend.domain.application.rest.expense.SuppliesExpenseController
import com.gms.backend.domain.domain.model.expense.SuppliesExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import java.util.UUID

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SuppliesExpenseMapper {
    @Mapping(target = "objectIds", source = "suppliesExpensesObjects")
    fun toReadDto(suppliesExpense: SuppliesExpense): SuppliesExpenseController.SuppliesExpenseReadDTO

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toSuppliesExpense(dto: SuppliesExpenseController.SuppliesExpenseCreateDTO): SuppliesExpense

    @Mapping(target = "updatedBy", ignore = true)
    fun updateSuppliesExpenseFromDto(dto: SuppliesExpenseController.SuppliesExpenseUpdateDTO, @MappingTarget entity: SuppliesExpense): SuppliesExpense

    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}