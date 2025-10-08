package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public final class DatabaseEnsurer {
    private DatabaseEnsurer() {}

    public static void ensureDatabase() {
        String defaultDbUrl = "jdbc:mysql://localhost:3306/foodapp";
        String fullUrl = System.getProperty("JDBC_URL", System.getenv().getOrDefault("JDBC_URL", defaultDbUrl));
        String user = System.getProperty("JDBC_USER", System.getenv().getOrDefault("JDBC_USER", "root"));
        String pass = System.getProperty("JDBC_PASSWORD", System.getenv().getOrDefault("JDBC_PASSWORD", "foodpass"));

        // Derive server URL without schema and extract db name
        String dbName = fullUrl.substring(fullUrl.lastIndexOf('/') + 1).split("[?]", 2)[0];
        String serverUrl = fullUrl.substring(0, fullUrl.lastIndexOf('/')) + "/";

        try (Connection conn = DriverManager.getConnection(serverUrl, user, pass);
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("✅ Ensured database exists: " + dbName);
        } catch (Exception e) {
            System.err.println("⚠ Unable to ensure database existence: " + e.getMessage());
        }
    }
}


