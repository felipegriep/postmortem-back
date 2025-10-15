package com.griep.postmortem.service;

import com.griep.postmortem.domain.dto.request.AuthLoginDTO;
import com.griep.postmortem.domain.dto.response.AuthLoginResponseDTO;
import com.griep.postmortem.domain.enums.ProviderEnum;
import com.griep.postmortem.domain.model.UserAccount;
import com.griep.postmortem.domain.util.TokenUtils;
import com.griep.postmortem.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {

    private final UserAccountRepository userRepo;

    @Value("${jwt.google.key}")
    private String jwtSecret;

    @Value("${jwt.google.expiresMinutes:60}")
    private long jwtExpMinutes;

    @Override
    public AuthLoginResponseDTO login(AuthLoginDTO request) {
        String idToken = request.getIdToken();
        ProviderEnum provider = request.getProvider();
        if (!StringUtils.hasText(idToken) || provider == null) {
            throw new IllegalArgumentException("Invalid login request");
        }

        String email = extractEmailFromIdToken(idToken);
        String subject = extractSubjectFromIdToken(idToken);
        String nameFromToken = extractStringClaim(idToken, "name");
        String pictureFromToken = extractStringClaim(idToken, "picture");

        if (!StringUtils.hasText(email) || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid idToken: missing or invalid email claim");
        }

        String name = StringUtils.hasText(nameFromToken) ? nameFromToken : email.substring(0, email.indexOf('@'));

        // Prefer find by externalId (subject) and provider if present
        UserAccount user = null;
        if (StringUtils.hasText(subject)) {
            user = userRepo.findByExternalIdAndProvider(subject, provider).orElse(null);
        }
        if (user == null) {
            user = userRepo.findByEmail(email).orElse(null);
        }
        if (user == null) {
            user = UserAccount.builder()
                    .email(email)
                    .name(name)
                    .provider(provider)
                    .externalId(subject)
                    .pictureUrl(pictureFromToken)
                    .active(true)
                    .build();
        }

        // update fields
        user.setProvider(provider);
        if (StringUtils.hasText(subject)) user.setExternalId(subject);
        if (!StringUtils.hasText(user.getName()) && StringUtils.hasText(name)) {
            user.setName(name);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            user.setEmail(email);
        }
        if (StringUtils.hasText(pictureFromToken)) {
            user.setPictureUrl(pictureFromToken);
        }
        user.setLastLoginAt(LocalDateTime.now());

        user = userRepo.save(user);

        // Generate JWT token for our app
        var claims = new HashMap<String, Object>();
        claims.put("sub", String.valueOf(user.getId()));
        claims.put("userId", user.getId());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("provider", user.getProvider().name());
        claims.put("pictureUrl", user.getPictureUrl());
        long exp = TokenUtils.epochSecondsPlusMinutes(jwtExpMinutes);
        claims.put("iat", System.currentTimeMillis() / 1000);
        claims.put("exp", exp);

        String token = TokenUtils.generateJwt(claims, jwtSecret);
        var resp = new AuthLoginResponseDTO();
        resp.setToken(token);
        return resp;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Extract email from provider JWT token payload
    private String extractEmailFromIdToken(String idToken) {
        return extractStringClaim(idToken, "email");
    }

    // Extract subject (external user id) from provider JWT token payload
    private String extractSubjectFromIdToken(String idToken) {
        return extractStringClaim(idToken, "sub");
    }

    private String extractStringClaim(String jwt, String claim) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return null;
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            String payload = new String(decoded, StandardCharsets.UTF_8);
            Pattern p = Pattern.compile("\"" + Pattern.quote(claim) + "\"\\s*:\\s*\"([^\"]*)\"");
            Matcher m = p.matcher(payload);
            if (m.find()) {
                return m.group(1);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
