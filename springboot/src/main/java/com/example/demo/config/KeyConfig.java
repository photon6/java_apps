package com.example.demo.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Configuration
public class KeyConfig {

    private static final String ISSUER = "https://www.propensic.io";

    private final KeyPair keyPair;

    public KeyConfig() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            this.keyPair = kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to initialize RSA key pair", e);
        }
    }

    @Bean
    public RSAPublicKey rsaPublicKey() {
        return (RSAPublicKey) keyPair.getPublic();
    }

    @Bean
    public RSAPrivateKey rsaPrivateKey() {
        return (RSAPrivateKey) keyPair.getPrivate();
    }

    public static String generateJsonWwbToken(String subject, String role) {
        // Implementation for generating a sample JWT token

        KeyConfig config = new KeyConfig();

        RSAPrivateKey privateKey = config.rsaPrivateKey();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(subject)
                .claim("role", role)
                .expirationTime(new java.util.Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour expiry
                .issuer(ISSUER)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new com.nimbusds.jose.JWSHeader(com.nimbusds.jose.JWSAlgorithm.RS256),
                claims);

        try {
            signedJWT.sign(new RSASSASigner(privateKey));
        } catch (JOSEException ex) {
            System.err.println(ex.getMessage());
        }

        return signedJWT.serialize();

    }
}
