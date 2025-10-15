package com.griep.postmortem.domain.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenUtils {

    private static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static byte[] hmacSha256(byte[] key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Error signing token", e);
        }
    }

    private static String toJson(Map<String, Object> map) {
        // Minimal JSON builder for simple values (String, Number, Boolean)
        String body = map.entrySet().stream().map(e -> {
            String key = escapeJson(e.getKey());
            Object val = e.getValue();
            String valueStr;
            if (val == null) {
                valueStr = "null";
            } else if (val instanceof Number || val instanceof Boolean) {
                valueStr = val.toString();
            } else {
                valueStr = '"' + escapeJson(String.valueOf(val)) + '"';
            }
            return '"' + key + '"' + ':' + valueStr;
        }).collect(Collectors.joining(","));
        return '{' + body + '}';
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static String generateJwt(Map<String, Object> claims, String secret) {
        String headerJson = toJson(Map.of(
                "alg", "HS256",
                "typ", "JWT"
        ));
        String payloadJson = toJson(claims);

        String header = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signingInput = header + "." + payload;
        String signature = base64UrlEncode(hmacSha256(secret.getBytes(StandardCharsets.UTF_8), signingInput));
        return signingInput + "." + signature;
    }

    public static long epochSecondsPlusMinutes(long minutes) {
        return Instant.now().plusSeconds(minutes * 60).getEpochSecond();
    }
}
