package com.example.demo.service.iml;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "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"; // Khóa bí mật của bạn

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes()) // Đặt khóa bí mật để giải mã
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public String getUserNameFromToken(String token) {
        return extractAllClaims(token).get("name", String.class);
    }

    public String getPictureFromToken(String token) {
        return extractAllClaims(token).get("picture", String.class);
    }
}
