package com.homework.basic.application.jwt;

import com.homework.basic.domain.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j(topic = "jwt-utils")
@Component
public class JwtUtils {

    @Value("${spring.application.name}")
    private String issuer;
    @Value("${jwt-secret-key}")
    private String key;
    @Value("${jwt-expiration-time}")
    private Long expirationTime;
    private SecretKey secretKey;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
    }

    public String createToken(String username, UserRole userRole) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", userRole.getAuthority())
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validationToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public Claims extractToken(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
