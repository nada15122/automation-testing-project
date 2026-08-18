package com.orangehrm.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

/** Loads test data from testdata.json using Jackson with safe fallback data. */
public final class JsonDataReader {

    private static final JsonNode ROOT;

    static {
        JsonNode tempRoot = null;
        try (InputStream input = JsonDataReader.class.getClassLoader()
                .getResourceAsStream("testdata.json")) {
            if (input != null) {
                tempRoot = new ObjectMapper().readTree(input);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not read testdata.json. Error: " + e.getMessage());
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

    /** Returns an array node as a TestNG DataProvider matrix of single values. */
    public static Object[][] arrayAsDataProvider(String key) {
        JsonNode array = node(key);

        if (array != null && array.isArray() && array.size() > 0) {
            Object[][] data = new Object[array.size()][1];
            for (int i = 0; i < array.size(); i++) {
                data[i][0] = array.get(i).asText();
            }
            return data;
        }

        // Fallback data in case the key is missing or empty
        if ("existingEmployees".equals(key)) {
            return new Object[][]{{"Nada"}};
        } else if ("nonExistingEmployees".equals(key)) {
            return new Object[][]{{"Zzqx Notreal"}, {"Ghost Employee 999"}};
        }

        return new Object[][]{};
    }

    /** Returns an array of login objects as {username, password} rows. */
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