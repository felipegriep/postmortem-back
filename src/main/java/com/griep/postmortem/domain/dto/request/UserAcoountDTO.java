package com.griep.postmortem.domain.dto.request;

import com.griep.postmortem.domain.enums.ProviderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserAcoountDTO {
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderEnum provider;

    private String externalId;

    @NotNull
    private String email;

    @NotNull
    private String name;

    private String pictureUrl;

    @NotNull
    private Boolean active;
}
