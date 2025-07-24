package model;

import java.util.Base64;

public class TokenUtil {
    public static String issueToken(User user) {
        String payload = user.getId() + ":" + user.getRole().name();
        return Base64.getEncoder().encodeToString(payload.getBytes());
    }
    public static long decodeUserId(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token.replace("Bearer ", "")));
            return Long.parseLong(decoded.split(":")[0]);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }
}
