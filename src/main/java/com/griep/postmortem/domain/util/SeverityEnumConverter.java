package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.enums.SeverityEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SeverityEnumConverter implements AttributeConverter<SeverityEnum, String> {
    @Override
    public String convertToDatabaseColumn(SeverityEnum attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public SeverityEnum convertToEntityAttribute(String dbValue) {
        return dbValue != null ? SeverityEnum.fromValue(dbValue) : null;
    }
}
