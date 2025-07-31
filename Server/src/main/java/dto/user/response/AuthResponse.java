package dto.user.response;

public record AuthResponse(String token, LoginResponseDto user) {}
