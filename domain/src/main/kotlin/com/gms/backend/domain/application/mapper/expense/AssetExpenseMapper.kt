package com.gms.backend.domain.application.mapper.expense

import com.gms.backend.domain.application.rest.expense.AssetExpenseController
import com.gms.backend.domain.domain.model.expense.AssetExpense
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper(componentModel = "spring")
interface AssetExpenseMapper {
    @Mapping(target = "objects", expression = "java(assetExpense.getObjects() != null ? assetExpense.getObjects() : Collections.emptyList())")
    fun toResponseDto(assetExpense: AssetExpense): AssetExpenseController.AssetExpenseResponseDTO
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toAssetExpense(dto: AssetExpenseController.AssetExpensePostDTO): AssetExpense
    fun toAssetSummary(assetExpense: AssetExpense): AssetExpenseController.AssetExpenseSummaryDTO
    fun updateAssetExpenseFromDto(dto: AssetExpenseController.AssetExpensePutDTO,
                                  @MappingTarget entity: AssetExpense): AssetExpense
}