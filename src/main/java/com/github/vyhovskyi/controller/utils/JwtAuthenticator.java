package com.github.vyhovskyi.controller.utils;

import com.github.vyhovskyi.controller.JwtHandler;
import com.github.vyhovskyi.service.ServiceFactory;
import com.github.vyhovskyi.service.UserService;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class JwtAuthenticator extends Authenticator {

    @Override
    public Result authenticate(HttpExchange exch) {
        String token = exch.getRequestHeaders().getFirst("Authorization");

        if (token == null || token.isBlank()) {
            return new Failure(403);
        }

        String username = JwtHandler.verify(token);

        if (username == null) {
            return new Failure(403);
        }

        UserService userService = ServiceFactory.getUserService();
        if (userService.isUserExist(username)) {
            return new Success(new HttpPrincipal(username, "default"));
        }
        return new Failure(403);
    }
}
