package network;

import com.fasterxml.jackson.databind.ObjectMapper;
import network.dto.order.UpdateOrderStatusRequestDto;
import util.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.List;

public class ApiClient {
    private static final ApiClient instance = new ApiClient();
    private static final String BASE_URL = initBaseUrl();
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }
    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }


    public static ApiClient getInstance() {
        return instance;
    }

    private static String initBaseUrl() {
        String prop = System.getProperty("API_BASE_URL");
        if (prop == null || prop.isBlank()) {
            prop = System.getenv("API_BASE_URL");
        }
        if (prop == null || prop.isBlank()) {
            prop = "http://localhost:8080";
        }
        if (!prop.endsWith("/")) prop = prop + "/";
        return prop;
    }

    public <T> HttpRequest buildPost(String path, T body) throws Exception {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));

        String token = SessionManager.getInstance().getToken();
        if (token != null) builder.header("Authorization", "Bearer " + token);

        return builder.build();
    }

    public <T> HttpRequest buildGet(String path) {
        System.out.println("[GET] " + BASE_URL + path);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
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
        if (responseType == Void.class || response.body() == null || response.body().isBlank()) {
            return null;
        }
        return objectMapper.readValue(response.body(), responseType);
    }
    public <T> List<T> sendList(HttpRequest request, Class<T> clazz) throws Exception {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            // Build a TypeReference<List<T>>
            return objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } else {
            throw new RuntimeException("HTTP error: " + response.statusCode() + "\n" + response.body());
        }
    }

    public <T> HttpRequest buildPatch(String path, T body) throws Exception {
        String json = getObjectMapper().writeValueAsString(body);

        return HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .headers(authHeaderKV())
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();
    }
    public <T> HttpRequest buildPut(String path, T body) throws Exception {
        String token = SessionManager.getInstance().getToken();
        String json = objectMapper.writeValueAsString(body);
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .headers(authHeaderKV())
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private static String[] authHeaderKV() {
        String token = SessionManager.getInstance().getToken();
        if (token == null || token.isBlank()) return new String[]{};
        return new String[]{"Authorization", "Bearer " + token};
    }

}
