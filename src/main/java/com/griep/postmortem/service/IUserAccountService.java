package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.UserAccountDTO;
import com.griep.postmortem.domain.dto.response.UserAccountResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserAccountService {
    Page<UserAccountResponseDTO> list(final String name, final String email, final Boolean active, final Pageable pageable);
    UserAccountResponseDTO get(final Long id);
    Optional<UserAccountResponseDTO> get(final String email, final String externalId);
    UserAccountResponseDTO create(final UserAccountDTO userAccount);
    UserAccountResponseDTO update(final Long id, final UserAccountDTO incident);
}
