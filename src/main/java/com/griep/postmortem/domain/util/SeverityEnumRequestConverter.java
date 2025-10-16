package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.enums.SeverityEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.griep.postmortem.domain.enums.SeverityEnum.fromValue;

@Component
public class SeverityEnumRequestConverter implements Converter<String, SeverityEnum> {
    @Override
    public SeverityEnum convert(String source) {
        if (source == null) {
            return null;
        }
        return fromValue(source);
    }
}
