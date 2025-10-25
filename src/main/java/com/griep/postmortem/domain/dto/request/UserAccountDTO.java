package com.griep.postmortem.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.griep.postmortem.domain.enums.ProviderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserAccountDTO {
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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime lastLoginAt;
}
