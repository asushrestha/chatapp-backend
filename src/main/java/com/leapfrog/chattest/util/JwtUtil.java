package com.leapfrog.chattest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final long serialVersionUID = -2550185165626007488L;

    private static String secret="MWFoMEc3YmpkaHY=";


    public  String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    public  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public  String getByKey(String token, String key) {
        Claims allClaimsFromToken = getAllClaimsFromToken(token);
        return (String) allClaimsFromToken.get(key);
    }
    //for retrieveing any information from token we will need the secret key
    private  Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //generate token for user
    public  String generateToken(Map<String, Object> claims) {
        System.out.println(claims);
        return doGenerateToken(claims,claims.get("userName").toString());
    }

    private  String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Long getId(String token) {
        Claims allClaimsFromToken  =getAllClaimsFromToken(token);
        return ((Number) allClaimsFromToken.get("id")).longValue();
    }
}
