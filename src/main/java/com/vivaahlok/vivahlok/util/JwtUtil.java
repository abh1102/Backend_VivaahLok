package com.vivaahlok.vivahlok.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

/**
 * @deprecated Use {@link com.vivaahlok.vivahlok.security.JwtTokenProvider} instead
 */
@Deprecated
public class JwtUtil {

    private static final String SECRET = "MY_SUPER_SECRET_KEY_FOR_JWT_256_BITS_LONG";

    public static String generateToken(String phone) {
        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
