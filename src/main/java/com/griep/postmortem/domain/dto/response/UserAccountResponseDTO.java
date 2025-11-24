package com.griep.postmortem.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.griep.postmortem.domain.enums.ProviderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountResponseDTO {

    private Long id;

    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    private String externalId;
    private String email;
    private String name;
    private String pictureUrl;
    private Boolean active;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
