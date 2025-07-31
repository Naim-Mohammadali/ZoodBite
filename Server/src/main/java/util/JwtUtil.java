package util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import model.Role;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

public final class JwtUtil {
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRY_MS = 4 * 60 * 60 * 1000;   // 4 h

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
