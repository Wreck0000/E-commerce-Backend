package com.ecom.security.jwt;

import com.ecom.security.user.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;

public class JwtUtils {
    private String JwtSecret;
    private Long ExpirationTime;
    public String generateTokenForUser(Authentication authentication){
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder().subject(userPrincipal.getEmail())
                .claim("id",userPrincipal.getId())
                .claim("roles",roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
    }
}
