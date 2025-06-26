package com.github.vyhovskyi.controller.utils;

import com.sun.net.httpserver.HttpExchange;

public class CommandKeyGenerator {

    private static final String CONTROLLER_PATH = ".*/controller/";
    private static final String REPLACEMENT = "";
    private static final String DELIMITER = ":";

    private CommandKeyGenerator() {}

    public static String generateCommandKeyFromRequest(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath().replaceAll(CONTROLLER_PATH, REPLACEMENT);
        String key = method + DELIMITER + path;
        System.out.println(key);
        return key;
    }
}
