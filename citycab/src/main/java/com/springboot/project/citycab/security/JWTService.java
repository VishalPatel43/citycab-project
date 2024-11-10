package com.springboot.project.citycab.security;


import com.springboot.project.citycab.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;  // Injected from application.properties or environment variables

    // Retrieve the secret key for signing the JWT token
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    // General method for creating a token (either access or refresh)
    private String createToken(Map<String, Object> claims, String subject, long expirationTimeInMs) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeInMs))
                .signWith(getSecretKey())
                .compact();
    }

    // Generate access token for user with 10 minutes validity
    public String generateAccessToken(User user) {
        Map<String, Object> claims = Map.of(
                "email", user.getUsername(),
//                "email", user.getEmail(),
                "roles", user.getRoles().toString());
        return createToken(claims, user.getUserId().toString(), 1000L * 60 * 60);  // 60 minutes validity
    }

    // Generate refresh token for user with 6 months validity
    public String generateRefreshToken(User user) {
        return createToken(
                Map.of(),
                user.getUserId().toString(),
                1000L * 60 * 60 * 24 * 30 * 6
        );  // 6 months validity
    }


    // Extract specific claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract username (email) from the token --> but subject is the userId
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    // Extract email from the token
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    // Extract userId (subject) from the token
    public Long getUserIdFromToken(String token) {
//        return extractClaim(token, claims -> claims.get("sub", Long.class));
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    // Extract expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the JWT token with UserDetails
    public boolean validateToken(String token, User user) {
        String email = extractEmail(token);
        return (email.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
