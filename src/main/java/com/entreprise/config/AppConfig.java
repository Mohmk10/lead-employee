package com.entreprise.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final String PROPERTIES_FILE = "/app.properties";
    private static final Properties props = new Properties();
    private static final EntityManagerFactory emf;

    static {
        try (InputStream in = AppConfig.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (in == null) throw new RuntimeException("app.properties introuvable");
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger app.properties", e);
        }
        emf = Persistence.createEntityManagerFactory("lead-employee-pu", props);
    }

    public static EntityManagerFactory getEntityManagerFactory() { return emf; }

    public static void close() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    public static Properties getProperties() { return props; }
}
