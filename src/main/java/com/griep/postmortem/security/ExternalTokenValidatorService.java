package com.griep.postmortem.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griep.postmortem.domain.enums.ProviderEnum;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class ExternalTokenValidatorService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ExternalProfile validateAndExtract(ProviderEnum provider, String idToken) {
        if (idToken == null || idToken.isBlank()) {
            throw new IllegalArgumentException("Invalid provider token");
        }
        // Minimal implementation: if token seems to be a JWT, decode payload and extract common fields
        if (idToken.split("\\.").length >= 2) {
            try {
                String payload = idToken.split("\\.")[1];
                String json = new String(Base64.decodeBase64(payload), StandardCharsets.UTF_8);
                JsonNode node = objectMapper.readTree(json);
                String email = optText(node, "email").orElse(null);
                String name = optText(node, "name").orElseGet(() -> optText(node, "given_name").orElse(null));
                String picture = optText(node, "picture").orElse(null);
                String sub = optText(node, "sub").orElse(null);
                if (email == null) {
                    // cannot proceed without email to map user
                    throw new IllegalArgumentException("Provider token payload missing email");
                }
                return new ExternalProfile(email, name != null ? name : email, picture, sub);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to decode provider token payload");
            }
        }
        // Fallback: accept as email if matches simple pattern
        if (idToken.contains("@")) {
            return new ExternalProfile(idToken, idToken, null, null);
        }
        throw new IllegalArgumentException("Unsupported provider token format");
    }

    private Optional<String> optText(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? Optional.of(node.get(field).asText()) : Optional.empty();
    }

    public record ExternalProfile(String email, String name, String pictureUrl, String externalId) {}
}
