package util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import model.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public final class JwtUtil {
    private static final Key KEY = buildKey();
    private static final long EXPIRY_MS = 4 * 60 * 60 * 1000;   // 4 h

    private static Key buildKey() {
        String secret = System.getProperty("JWT_SECRET");
        if (secret == null || secret.isBlank()) {
            secret = System.getenv("JWT_SECRET");
        }
        if (secret == null || secret.isBlank()) {
            // Fallback for dev; rotate on restart
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        byte[] bytes = secret.startsWith("base64:")
                ? Base64.getDecoder().decode(secret.substring("base64:".length()))
                : secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    public static String issueToken(long userId, Role role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role.name())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_MS))
                .signWith(KEY)
                .compact();
    }

    public static Jws<Claims> parse(String jwt) {
        return Jwts.parser().verifyWith((SecretKey) KEY).build().parseSignedClaims(jwt);
    }
}
