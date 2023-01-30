package org.example.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceReader {
    public static String readApplicationProperties(String variableName) {
        String fileName = "application.properties";
        Map<String, String> map;
        URL url = ResourceReader.class.getClassLoader().getResource(fileName);
        if (url == null)
            throw new RuntimeException("file " + fileName + " was not found or access to the resource is denied");
        try (Stream<String> stream = Files.lines(Paths.get(url.toURI()))) {
            map = stream.map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("error occurs opening the file " + fileName, e);
        }
        return map.get(variableName);
    }
}
