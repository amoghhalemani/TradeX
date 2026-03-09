package com.tradex.tradex.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.function.Function;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String GenerateToken(String identifier)
    {
        return buildToken(new HashMap<>(), identifier, jwtExpiration);
    }
    private String buildToken(Map<String ,Object> extraClaims, String identifier, long expiration)
    {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(identifier)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the Username  from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validate if the token belongs to this user AND is not expired
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Check if the token's death date has passed
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the exact expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // A generic tool to pull ANY specific piece of data out of the payload
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // THE CORE ENGINE: This is the math that actually breaks the token open
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
