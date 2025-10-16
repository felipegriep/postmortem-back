package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.enums.StatusEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.griep.postmortem.domain.enums.StatusEnum.fromValue;

@Component
public class StatusEnumRequestConverter implements Converter<String, StatusEnum> {
    @Override
    public StatusEnum convert(String source) {
        if (source == null) {
            return null;
        }
        return fromValue(source);
    }
}
