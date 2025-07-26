package util;

import network.dto.user.UserDto;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private String token;
    private UserDto loggedInUser;

    public static SessionManager getInstance() { return instance; }

    public void setToken(String token) { this.token = token; }

    public String getToken() { return token; }

    public UserDto getLoggedInUser() { return loggedInUser; }

    public void setLoggedInUser(UserDto user) { this.loggedInUser = user; }

    public void clear() {
        token = null;
        loggedInUser = null;
    }
}
