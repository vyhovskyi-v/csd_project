package com.github.vyhovskyi.controller;

import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.CommandKeyGenerator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ProcessRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String commandKey = CommandKeyGenerator.generateCommandKeyFromRequest(exchange);
        Command command = CommandFactory.getCommand(commandKey);
        command.execute(exchange);

    }
}
