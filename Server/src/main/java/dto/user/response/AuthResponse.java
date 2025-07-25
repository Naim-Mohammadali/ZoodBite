package dto.user.response;

import model.Role;
import model.User;
import model.User.Status;

public record AuthResponse(String token, User user) {}
