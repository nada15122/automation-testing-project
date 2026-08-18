package com.orangehrm.utils;

import java.io.InputStream;
import java.util.Properties;

/** Reads environment settings from src/test/resources/config.properties. */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found in test resources");
            }
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String browser() {
        return get("browser");
    }

    public static int explicitWait() {
        return Integer.parseInt(get("explicit.wait"));
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }
}
