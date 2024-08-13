package com.pilot2023.xt.mapper;

import com.pilot2023.xt.exception.BusinessException;
import com.pilot2023.xt.exception.ExceptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExpenseMapperEntryPointRest {

    @Mapping(target = "status", source = "httpStatusCode")
    ExceptionDto toExceptionDto(BusinessException e);

}