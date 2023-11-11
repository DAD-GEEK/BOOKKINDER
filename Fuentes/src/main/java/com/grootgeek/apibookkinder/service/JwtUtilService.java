package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.UserAPIEntity;
import com.grootgeek.apibookkinder.entities.UserAppEntity;
import com.grootgeek.apibookkinder.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    @Value("${application.name}")
    private String applicationName;
    private final Utils utilLogs = new Utils();
    @Value("${token.secrect.key}")
    private String secretKey;
    @Value("${token.time}")
    private int timeToken;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        try {
            claims.put("AppName", applicationName);
            return createToken(claims, userDetails.getUsername());
        } catch (Exception e) {
            utilLogs.logApiError("No se pudo crear el token" + e.getMessage());
            logger.info(String.valueOf(e));
            return null;
        }
    }

    public String generateToken(UserAppEntity UserAppEntity) {
        Map<String, Object> claims = new HashMap<>();
        try {
            claims.put("AppName", applicationName);
            return createToken(claims, UserAppEntity.getEmail());
        } catch (Exception e) {
            utilLogs.logApiError("No se pudo crear el token" + e.getMessage());
            logger.info(String.valueOf(e));
            return null;
        }
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + timeToken)).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
