package com.projects.socialmediaapi.security.jwt.utils;


import com.projects.socialmediaapi.user.models.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import static com.projects.socialmediaapi.security.jwt.utils.MapUtils.toClaims;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwt_secret;
    @Value("${jwt.issuer}")
    private String jwt_issuer;
    @Value("${jwt.expirationMs}")
    private Long jwt_expirationMs;


    public String generateToken(UserDetails userDetails, Person person) {
        return Jwts.builder()
                .setClaims(toClaims(person))
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .setIssuer(jwt_issuer)
                .setIssuedAt(getNowDate())
                .setExpiration(getExpirationDate())
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private boolean isEqualsUsername(UserDetails userDetails, String token) {
        return extractUsername(token).equals(userDetails.getUsername());
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(getNowDate());

    }

    private Date getExpirationDate() {
        return Date.from(ZonedDateTime.now().toInstant().plusMillis(jwt_expirationMs));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return isEqualsUsername(userDetails, token) && !isTokenExpired(token);
    }

    private Date getNowDate() {
        return Date.from(ZonedDateTime.now().toInstant());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(jwt_secret);
        return Keys.hmacShaKeyFor(bytes);
    }
}
