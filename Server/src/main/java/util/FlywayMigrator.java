package util;

import org.flywaydb.core.Flyway;

public final class FlywayMigrator {
    private FlywayMigrator() {}

    public static void migrate() {
        String url = System.getProperty("JDBC_URL", System.getenv().getOrDefault("JDBC_URL", "jdbc:mysql://localhost:3306/foodapp"));
        String user = System.getProperty("JDBC_USER", System.getenv().getOrDefault("JDBC_USER", "root"));
        String pass = System.getProperty("JDBC_PASSWORD", System.getenv().getOrDefault("JDBC_PASSWORD", "foodpass"));

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, pass)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }
}


