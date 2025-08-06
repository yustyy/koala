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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.password-set.expiration-ms}")
    private Long PASSWORD_SET_EXPIRATION_MS;

    @Value("${jwt.password-reset.expiration-ms}")
    private Long PASSWORD_RESET_EXPIRATION_MS;


    public String generatePasswordSetToken(UUID id) {
        return Jwts.builder()
                .subject(id.toString())
                .issuer("iskoala.com")
                .audience().add("password-set")
                .and().issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + PASSWORD_SET_EXPIRATION_MS))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    public UUID extractPendingRegistrationId(String token) {
       Claims claims = extractAllClaims(token);

       if (!Set.of("password-set").equals(claims.getAudience())){
              throw new IllegalArgumentException("Invalid token type");
       }
       return UUID.fromString(claims.getSubject());
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        try{
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true; // If there's an error, we assume the token is expired
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {

        final Claims claims = Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims;
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
