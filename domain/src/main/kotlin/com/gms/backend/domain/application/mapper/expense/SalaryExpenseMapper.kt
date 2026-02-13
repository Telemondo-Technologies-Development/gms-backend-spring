package com.gms.backend.domain.application.mapper.expense

import com.gms.backend.domain.application.rest.expense.SalaryExpenseController
import com.gms.backend.domain.domain.model.expense.SalaryExpense
import com.gms.backend.domain.domain.model.storage.ObjectStorage
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import java.util.UUID

@Mapper(componentModel = "spring")
interface SalaryExpenseMapper {
    @Mapping(target = "objectIds", source = "salaryExpensesObjects")
    fun toReadDto(salaryExpense: SalaryExpense): SalaryExpenseController.SalaryExpenseReadDTO
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toSalaryExpense(dto: SalaryExpenseController.SalaryExpenseCreateDTO): SalaryExpense
    @Mapping(target = "updatedBy", ignore = true)
    fun updateSalaryExpenseFromDto(dto: SalaryExpenseController.SalaryExpenseUpdateDTO, @MappingTarget entity: SalaryExpense): SalaryExpense
    fun mapObjectsToIds(objects: Set<ObjectStorage>?): List<UUID> {
        return objects?.map { it.id } ?: emptyList()
    }
}