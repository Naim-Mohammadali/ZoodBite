package model;

public class TokenUtil {
    public static String issueToken(User user) {
        return util.JwtUtil.issueToken(user.getId(), user.getRole());
    }
    public static long decodeUserId(String token) {
        String raw = token.replace("Bearer ", "");
        return Long.parseLong(util.JwtUtil.parse(raw).getPayload().getSubject());
    }
}
