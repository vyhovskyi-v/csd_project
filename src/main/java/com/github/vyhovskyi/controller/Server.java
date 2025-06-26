package com.github.vyhovskyi.controller;

import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;


import com.sun.net.httpserver.HttpServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class Server {
//    public static void main(String[] args) throws IOException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);
//
//        server.createContext("/controller", new ProcessRequestHandler());
//
//        server.start();
//
//    }

    public static void main(String[] args) throws Exception {

        // Параметри для keystore
        String keystorePath = "keystore.jks"; // якщо лежить у корені проєкту
        char[] password = "password".toCharArray(); // твій пароль з keytool

        // Завантаження keystore
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            ks.load(fis, password);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        // Налаштування HTTPS-сервера
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8443), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                params.setSSLParameters(sslContext.getDefaultSSLParameters());
            }
        });

        server.createContext("/controller", new ProcessRequestHandler());
        server.start();

        System.out.println("HTTPS-сервер запущено на порті 8443");
    }
}
