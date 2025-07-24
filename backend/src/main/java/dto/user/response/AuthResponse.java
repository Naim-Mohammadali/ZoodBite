package dto.user.response;

import model.Role;
import model.User.Status;

public record AuthResponse(Long user_id, String token) {}
