package com.example.demo.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/secure-data")
    public Map<String, Object> getSecureData(@AuthenticationPrincipal Jwt jwt) {
        // In a real application, you would check the user's authentication and authorization here
        return Map.of(
                "message", "This is secure data that only authenticated users should see.",
                "subject", jwt.getSubject(),
                "issuer", jwt.getIssuer().toString(),
                "audience", jwt.getAudience(),
                "claims", jwt.getClaims()
        );
    }
}
