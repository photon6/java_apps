package com.example.demo.controller;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

//@RestController
public class OldSecureController {

    private final RSAPublicKey publicKey; // Assume injected

    public OldSecureController(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    private JWTClaimsSet validateToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        RSASSAVerifier verifier = new RSASSAVerifier(publicKey);

        if (!signedJWT.verify(verifier)) {
            throw new SecurityException("Invalid signature");
        }

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        if (claims.getExpirationTime().before(new java.util.Date())) {
            throw new SecurityException("Token expired");
        }

        return claims;
    }

    //@GetMapping("/secure-data")
    public ResponseEntity<String> getSecureData(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            JWTClaimsSet claims = validateToken(token);

            List<String> roles = (List<String>) claims.getClaim("roles");
            if (roles != null && roles.contains("ADMIN")) {
                return ResponseEntity.ok("Sensitive IAM data here");
            } else {
                return ResponseEntity.status(403).body("Forbidden");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }

    @GetMapping("/valdiate-token")
    public String validateJwt(@AuthenticationPrincipal JWT jwt) throws Exception {
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        return "Hello, " + claims.getSubject() + ". Role: " + claims.getClaim("role");
    }
}
