package com.allo.restaurant.menu.helpers;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class MockHelper {

    public String readFile(String filePath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("File not found in test resources: " + filePath);
            }
            return new String(inputStream.readAllBytes());
        }
    }
}
