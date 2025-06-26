package com.github.vyhovskyi.controller.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.entity.User;
import com.github.vyhovskyi.controller.JwtHandler;
import com.github.vyhovskyi.service.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;


public class LoginHandler implements HttpHandler {
    UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    if (!exchange.getRequestMethod().equals("POST")) {
        exchange.sendResponseHeaders(405,0);
        exchange.close();
        return;
    }

        ObjectMapper mapper = new ObjectMapper();

        try(var requestBody = exchange.getRequestBody()) {
            User user = mapper.readValue(requestBody, User.class);


            if (!userService.authenticate(user)) {
                exchange.sendResponseHeaders(401,0);
                exchange.close();
            }

            String token = JwtHandler.create(user.getUsername());
            exchange.getResponseHeaders().add("X-Token", token);
            exchange.sendResponseHeaders(200,0);
            exchange.close();



        }catch (IOException e){
            exchange.sendResponseHeaders(400,0);
            exchange.close();
        }









    }

}
