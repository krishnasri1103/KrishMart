package com.krishna.krishmart.listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Properties;

/** Owns the application's singleton HikariCP pool for its full servlet context lifecycle. */
public class DataSourceContextListener implements ServletContextListener {
    /** Servlet context attribute containing the pooled data source. */
    public static final String DATA_SOURCE_ATTRIBUTE = "krishmartDataSource";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextListener.class);
    private HikariDataSource dataSource;

    /** Creates the pool and applies schema and idempotent seed data. */
    @Override public void contextInitialized(ServletContextEvent event) {
        try {
            Properties props = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (input != null) props.load(input);
            }
            String url = envOrProperty("KRISHMART_DB_URL", props, "db.url", "jdbc:h2:./data/krishmart;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE");
            String username = envOrProperty("KRISHMART_DB_USERNAME", props, "db.username", "sa");
            String password = envOrProperty("KRISHMART_DB_PASSWORD", props, "db.password", "");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url); config.setUsername(username); config.setPassword(password);
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(Integer.parseInt(props.getProperty("pool.maximumSize", "10")));
            dataSource = new HikariDataSource(config);
            try (Connection connection = dataSource.getConnection();
                 InputStream schema = getClass().getClassLoader().getResourceAsStream("schema.sql");
                 InputStream seed = getClass().getClassLoader().getResourceAsStream("seed.sql")) {
                RunScript.execute(connection, new InputStreamReader(schema, StandardCharsets.UTF_8));
                RunScript.execute(connection, new InputStreamReader(seed, StandardCharsets.UTF_8));
            }
            event.getServletContext().setAttribute(DATA_SOURCE_ATTRIBUTE, dataSource);
            LOGGER.info("KrishMart database pool initialized.");
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to initialize KrishMart database.", exception);
        }
    }
    private String envOrProperty(String env, Properties props, String key, String fallback) {
        String value = System.getenv(env);
        return value != null ? value : props.getProperty(key, fallback);
    }
    /** Closes the pool exactly once when Tomcat stops the application. */
    @Override public void contextDestroyed(ServletContextEvent event) { if (dataSource != null) dataSource.close(); }
}