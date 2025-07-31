package util;

import network.dto.user.UserDto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();
    private String token;
    private UserDto loggedInUser;
    private final File tempFile = new File("temp-session.txt");

    public static SessionManager getInstance() { return instance; }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() { return token; }

    public UserDto getLoggedInUser() { return loggedInUser; }

    public void setLoggedInUser(UserDto user) {
        this.loggedInUser = user;
    }

    public void clear() {
        token = null;
        loggedInUser = null;
    }
    public void writeSessionToTemp() {
        if (token == null || loggedInUser == null) return;

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Token=" + token + "\n");

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(loggedInUser);
            writer.write("User=" + json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTempFile() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}
