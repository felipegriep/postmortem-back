package com.griep.postmortem.domain.util;

import com.griep.postmortem.domain.dto.request.UserAccountDTO;
import com.griep.postmortem.domain.dto.response.UserAccountResponseDTO;
import com.griep.postmortem.domain.model.UserAccount;
import org.modelmapper.ModelMapper;

public class UserAccountMapper {

    private final static ModelMapper mappeer;

    static {
        mappeer = new ModelMapper();
    }

    public static UserAccountResponseDTO toDTO(final UserAccount userAccount) {
        return mappeer.map(userAccount, UserAccountResponseDTO.class);
    }

    public static UserAccount toEntity(final UserAccount userAccount, final UserAccountDTO dto) {
        return userAccount.toBuilder()
                .provider(dto.getProvider())
                .externalId(dto.getExternalId())
                .email(dto.getEmail())
                .name(dto.getName())
                .pictureUrl(dto.getPictureUrl())
                .active(dto.getActive())
                .lastLoginAt(dto.getLastLoginAt())
                .build();
    }

    public static UserAccountDTO fromResponse(final UserAccountResponseDTO response) {
        return mappeer.map(response, UserAccountDTO.class);
    }
}
