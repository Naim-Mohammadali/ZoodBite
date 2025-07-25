package network;

import com.fasterxml.jackson.databind.ObjectMapper;
import util.SessionManager;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;

public class ApiClient {
    private static final ApiClient instance = new ApiClient();
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    public static ApiClient getInstance() {
        return instance;
    }

    public <T> HttpRequest buildPost(String path, T body) throws Exception {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/" + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));

        String token = SessionManager.getInstance().getToken();
        if (token != null) builder.header("Authorization", "Bearer " + token);

        return builder.build();
    }

    public <T> HttpRequest buildGet(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/" + path))
                .timeout(Duration.ofSeconds(10))
                .GET();

        String token = SessionManager.getInstance().getToken();
        if (token != null) builder.header("Authorization", "Bearer " + token);

        return builder.build();
    }

    public <T> T send(HttpRequest request, Class<T> responseType) throws Exception {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) {
            throw new RuntimeException("HTTP error: " + response.statusCode() + "\n" + response.body());
        }
        return objectMapper.readValue(response.body(), responseType);
    }
}
