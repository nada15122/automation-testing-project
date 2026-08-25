package com.orangehrm.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public final class JsonDataReader {

    private static final Logger log = LogManager.getLogger(JsonDataReader.class);
    private static final JsonNode ROOT;

    static {
        JsonNode tempRoot = null;
        try (InputStream input = JsonDataReader.class.getClassLoader()
                .getResourceAsStream("testdata.json")) {
            if (input != null) {
                tempRoot = new ObjectMapper().readTree(input);
            }
        } catch (Exception e) {
            log.error("Failed to read testdata.json file", e);
        }
        ROOT = tempRoot;
    }

    private JsonDataReader() {
    }

    public static JsonNode node(String key) {
        if (ROOT == null) {
            return null;
        }
        return ROOT.get(key);
    }

    public static String text(String parentKey, String childKey) {
        JsonNode parentNode = node(parentKey);
        if (parentNode != null && parentNode.has(childKey)) {
            return parentNode.get(childKey).asText();
        }
        return "";
    }

    public static Object[][] arrayAsDataProvider(String key) {
        JsonNode array = node(key);

        if (array != null && array.isArray() && array.size() > 0) {
            Object[][] data = new Object[array.size()][1];
            for (int i = 0; i < array.size(); i++) {
                data[i][0] = array.get(i).asText();
            }
            return data;
        }

        if ("existingEmployee".equals(key)) {
            return new Object[][]{{"Charlotte Smith"}};
        } else if ("nonExistingEmployee".equals(key)) {
            return new Object[][]{{"Zzqx Notreal"}};
        }

        return new Object[][]{};
    }

    public static Object[][] credentialsAsDataProvider(String key) {
        JsonNode array = node(key);

        if (array != null && array.isArray() && array.size() > 0) {
            Object[][] data = new Object[array.size()][2];
            for (int i = 0; i < array.size(); i++) {
                JsonNode item = array.get(i);
                data[i][0] = item.has("username") ? item.get("username").asText() : "";
                data[i][1] = item.has("password") ? item.get("password").asText() : "";
            }
            return data;
        }

        return new Object[][]{{"Admin", "admin123"}};
    }
}