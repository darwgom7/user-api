package com.darwgom.userapi.domain.converters;

import com.darwgom.userapi.domain.enums.RoleNameEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleNameConverter implements AttributeConverter<RoleNameEnum, String> {

    @Override
    public String convertToDatabaseColumn(RoleNameEnum roleName) {
        if (roleName == null) {
            return null;
        }
        return roleName.getValue();
    }

    @Override
    public RoleNameEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return RoleNameEnum.valueOf(dbData.toUpperCase());
    }
}
