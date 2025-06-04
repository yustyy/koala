package com.exskylab.koala.core.security;

import com.exskylab.koala.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.expiration}")
    private Long TOKEN_EXPIRATION;


    public String generateRefreshToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_EXPIRATION))
                .signWith(getSignKey())
                .compact();
    }

    public String generateTokenFromRefreshToken(String refreshToken){
        if (!isTokenValid(refreshToken)){
            throw new IllegalArgumentException("Geçersiz refresh token veya süresi dolmuş.");
        }

        String username = extractUser(refreshToken);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", username);
        return createToken(claims, username);

    }


    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getUsername());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        String email = extractUser(token);
        Date expirationDate = extractExpiration(token);
        return userDetails.getUsername().equals(email) && !expirationDate.before(new Date());
    }

    public Date extractExpiration(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration();
    }
    public String extractUser(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
    private String createToken(Map<String, Object> claims, String email) {
        var result = Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .signWith(getSignKey())
                .compact();
        return result;
    }
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String refreshToken) {
        try {
            extractExpiration(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
