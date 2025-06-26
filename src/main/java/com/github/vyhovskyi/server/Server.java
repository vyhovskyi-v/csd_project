package com.github.vyhovskyi.server;

import com.github.vyhovskyi.server.handlers.ApiGoodHandler;
import com.github.vyhovskyi.server.handlers.LoginHandler;
import com.github.vyhovskyi.service.ServiceFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

        server.createContext("/login", new LoginHandler(ServiceFactory.getUserService()));

        server.createContext("/api/good", new ApiGoodHandler(ServiceFactory.getProductService())).setAuthenticator(new JwtAuthenticator());
        server.start();

    }
}
