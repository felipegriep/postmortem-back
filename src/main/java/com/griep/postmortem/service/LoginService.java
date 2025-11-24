package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.AuthLoginDTO;
import com.griep.postmortem.domain.dto.request.UserAccountDTO;
import com.griep.postmortem.domain.dto.response.AuthLoginResponseDTO;
import com.griep.postmortem.domain.dto.response.UserAccountResponseDTO;
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

        var user = userAccountService.get(profile.email(), profile.externalId())
                .map(existingUser -> updateExistingUser(existingUser, profile))
                .orElseGet(() -> createNewUser(request, profile));

        String token = jwtTokenProvider.generateToken(user.getEmail(), buildClaims(user));

        return new AuthLoginResponseDTO(token);
    }

    private UserAccountResponseDTO createNewUser(AuthLoginDTO request, ExternalTokenValidatorService.ExternalProfile profile) {
        UserAccountDTO newUser = UserAccountDTO.builder()
                .email(profile.email())
                .name(profile.name())
                .provider(Optional.ofNullable(request.getProvider()).orElse(LOCAL))
                .externalId(profile.externalId())
                .pictureUrl(profile.pictureUrl())
                .active(true)
                .lastLoginAt(now())
                .build();

        return userAccountService.create(newUser);
    }

    private UserAccountResponseDTO updateExistingUser(UserAccountResponseDTO existingUser, ExternalTokenValidatorService.ExternalProfile profile) {
        UserAccountDTO updateUser = UserAccountDTO.builder()
                .email(existingUser.getEmail())
                .name(profile.name())
                .provider(existingUser.getProvider())
                .externalId(existingUser.getExternalId())
                .pictureUrl(profile.pictureUrl())
                .active(true)
                .lastLoginAt(now())
                .build();

        return userAccountService.update(existingUser.getId(), updateUser);
    }

    private HashMap<String, Object> buildClaims(UserAccountResponseDTO user) {
        var claims = new HashMap<String, Object>();
        claims.put("uid", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("pictureUrl", user.getPictureUrl());
        return claims;
    }
}
