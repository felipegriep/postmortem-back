package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.AuthLoginDTO;
import com.griep.postmortem.domain.dto.response.AuthLoginResponseDTO;

public interface ILoginService {
    AuthLoginResponseDTO login(AuthLoginDTO request);
}
