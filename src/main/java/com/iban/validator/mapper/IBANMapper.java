package com.iban.validator.mapper;

import org.mapstruct.Mapper;
import com.iban.validator.dto.IBANDto;
import com.iban.validator.entity.IBANEntity;

@Mapper(componentModel = "spring")
public interface IBANMapper {

    IBANDto toDto(IBANEntity ibanData);
}
