package com.dpvn.authservice;

import com.dpvn.crm.crudservice.domain.dto.CrmUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.timeout}")
  private long secretTimeout;
  @Value("${jwt.timeout}")
  private long refreshTimeout;

  public String generateToken(CrmUserDto user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId());
    claims.put("username", user.getUsername());
    claims.put("fullName", user.getFullName());
    claims.put("email", user.getEmail());
    claims.put("mobilePhone", user.getMobilePhone());
    claims.put("role", user.getRoleId());
    claims.put("department", user.getDepartmentId());
    return createToken(claims, user.getEmail(), secretTimeout);
  }

  public String generateRefreshToken(CrmUserDto user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId());
    claims.put("username", user.getUsername());
    return createToken(claims, user.getUsername(), refreshTimeout);
  }

  private String createToken(Map<String, Object> claims, String subject, long timeout) {
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
    Date now = new Date();
    Date validity = new Date(now.getTime() + timeout * 60 * 1000);
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(now)
        .expiration(validity)
        .signWith(key)
        .compact();
  }

  public long getExpirationTime(String token) {
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
    Jws<Claims> claimsJws = Jwts.parser()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);
    return claimsJws.getBody().getExpiration().getTime();
  }
}

