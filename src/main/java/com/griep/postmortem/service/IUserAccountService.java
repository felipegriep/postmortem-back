package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.UserAcoountDTO;
import com.griep.postmortem.domain.dto.response.UserAccountResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserAccountService {
    Page<UserAccountResponseDTO> list(final String name, final String email, final Boolean active, final Pageable pageable);
    UserAccountResponseDTO get(final Long id);
    Long create(final UserAcoountDTO userAccount);
    UserAccountResponseDTO update(final Long id, final UserAcoountDTO incident);
}
