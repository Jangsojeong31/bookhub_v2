package com.bookhub.bookhub_back.security;

import com.bookhub.bookhub_back.entity.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final Key key;
    private final int jwtExpirationMs;
    private final long jwtEmailExpirationMs;

    public int getExpiration() { return jwtExpirationMs; }

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") int jwtExpirationMs,
            @Value("${jwt.email-expiration-ms}") long jwtEmailExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtEmailExpirationMs = jwtEmailExpirationMs;
    }

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .claim("loginId", userPrincipal.getLoginId())
                .claim("role", userPrincipal.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateEmailValidToken(String email, String phoneNumber, String loginId) {
        return Jwts.builder()
                .claim("email", email)
                .claim("phoneNumber", phoneNumber)
                .claim("loginId", loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtEmailExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateEmailValidToken(String email, String loginId) {
        return Jwts.builder()
                .claim("email", email)
                .claim("loginId", loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtEmailExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String removeBearer(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid JWT token format");
        }
        return bearerToken.substring("Bearer ".length());
    }

    public boolean isValidToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String getLoginIdFromJwt(String token) {
        Claims claims = getClaims(token);
        return claims.get("loginId", String.class);
    }

    public String getRolesFromJwt(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }
}