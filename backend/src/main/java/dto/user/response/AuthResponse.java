package dto.user.response;

import model.Role;
import model.User.Status;

public record AuthResponse(String token, Role role, Status status) {}
