package com.github.vyhovskyi.controller.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class HttpSender {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendHtmlPage(HttpExchange exchange, String page) throws IOException {
        byte[] responseBytes = readFile(page);

        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    public static void sendJson(HttpExchange exchange, int statusCode, Object data ) throws IOException {
        byte [] responseBytes = mapper.writeValueAsBytes(data);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    public static void sendErrors(HttpExchange exchange, int statusCode, List<String> errors) throws IOException {
        Map<String, Object> responseMap = Map.of("errors", errors);

        byte[] responseBody = mapper.writeValueAsBytes(responseMap);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBody.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBody);
        }
    }


    private static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}
