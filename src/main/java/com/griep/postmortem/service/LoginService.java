package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.AuthLoginDTO;
import com.griep.postmortem.domain.dto.request.UserAccountDTO;
import com.griep.postmortem.domain.dto.response.AuthLoginResponseDTO;
import com.griep.postmortem.security.ExternalTokenValidatorService;
import com.griep.postmortem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

import static com.griep.postmortem.domain.enums.ProviderEnum.LOCAL;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {

    private final ExternalTokenValidatorService externalValidator;
    private final IUserAccountService userAccountService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthLoginResponseDTO login(AuthLoginDTO request) {
        var profile = externalValidator.validateAndExtract(request.getProvider(), request.getIdToken());

        // find or create user by email
        var user = userAccountService.get(profile.email(), profile.externalId());

        if (user.isEmpty()) {
            UserAccountDTO newUser = UserAccountDTO.builder()
                    .email(profile.email())
                    .name(profile.name())
                    .provider(request.getProvider() != null ?
                            request.getProvider() :
                            LOCAL)
                    .externalId(profile.externalId())
                    .pictureUrl(profile.pictureUrl())
                    .active(true)
                    .lastLoginAt(now())
                    .build();
            user = Optional.of(userAccountService.create(newUser));
        } else {
            UserAccountDTO updateUser = UserAccountDTO.builder()
                    .email(user.get().getEmail())
                    .name(profile.name())
                    .provider(user.get().getProvider())
                    .externalId(user.get().getExternalId())
                    .pictureUrl(profile.pictureUrl())
                    .active(true)
                    .lastLoginAt(now())
                    .build();
            user = Optional.of(userAccountService.update(user.get().getId(), updateUser));
        }

        var claims = new HashMap<String, Object>();
        claims.put("uid", user.get().getId());
        claims.put("email", user.get().getEmail());
        claims.put("name", user.get().getName());
        claims.put("pictureUrl", user.get().getPictureUrl());

        String token = jwtTokenProvider.generateToken(user.get().getEmail(), claims);

        return new AuthLoginResponseDTO(token);
    }
}
