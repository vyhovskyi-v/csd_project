package com.github.vyhovskyi.server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.ProductService;
import com.github.vyhovskyi.validator.ProductValidator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ApiGoodHandler implements HttpHandler {
    ProductService productService;

    public ApiGoodHandler(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()){
            case "GET" -> doGet(exchange);
            case "POST" -> doPost(exchange);
            case "PUT" -> doPut(exchange);
            case "DELETE" -> doDelete(exchange);
            default -> exchange.sendResponseHeaders(405, 0);
        }

        exchange.close();
    }

    private void doGet(HttpExchange exchange) throws IOException {
        String[] fragments = exchange.getRequestURI().getPath().split("/");
        if (fragments.length != 4){
            exchange.sendResponseHeaders(404,0);
            exchange.close();
            return;
        }
        try{
            int id = Integer.parseInt(fragments[3]);
            Optional<Product> productOptional = productService.getProductById(id);
            if (!productOptional.isPresent()){
                exchange.sendResponseHeaders(404,0);
                exchange.close();
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(productOptional.get());
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.getResponseBody().write(bytes);

        }catch (NumberFormatException e){
            exchange.sendResponseHeaders(400,0);
            exchange.close();
            return;
        }
    }

    private void doPost(HttpExchange exchange) throws IOException {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(exchange.getRequestBody(), Product.class);

            if (!ProductValidator.isValid(product)){
                exchange.sendResponseHeaders(409,0);
                exchange.close();
                return;
            }

            String error = validateProduct(product, "add");
            if (error != null){
                byte [] responseBody = responseErrorBody(error);
                exchange.sendResponseHeaders(400,responseBody.length);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.getResponseBody().write(responseBody);
                exchange.close();
                return;
            }


            Integer id = null;
            try{
                id = productService.createProduct(product);
            }catch (ServiceException e){
                exchange.sendResponseHeaders(500,0);
                exchange.close();
                return;
            }

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", id);

            byte [] responseBody = mapper.writeValueAsBytes(responseMap);
            exchange.sendResponseHeaders(201, responseBody.length);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.getResponseBody().write(responseBody);


        }catch (IOException e){
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
            return;
        }

    }


    private void doPut(HttpExchange exchange) throws IOException {
        String[] fragments = exchange.getRequestURI().getPath().split("/");
        if (fragments.length != 4){
            exchange.sendResponseHeaders(404,0);
            exchange.close();
            return;
        }
        try{
            int id = Integer.parseInt(fragments[3]);
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(exchange.getRequestBody(), Product.class);
            product.setId(id);

            if (!ProductValidator.isValidForUpdate(product)){
                exchange.sendResponseHeaders(409,0);
                exchange.close();
                return;
            }

            String error = validateProduct(product, "update");
            if (error != null){
                byte [] responseBody = responseErrorBody(error);
                exchange.sendResponseHeaders(400,responseBody.length);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.getResponseBody().write(responseBody);
                exchange.close();
                return;
            }


            productService.updateProduct(product);
            exchange.sendResponseHeaders(204, 0);


        }catch (IOException | NumberFormatException e){
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
            return;
        }
    }

    private void doDelete(HttpExchange exchange) throws IOException {
        String[] fragments = exchange.getRequestURI().getPath().split("/");
        if (fragments.length != 4){
            exchange.sendResponseHeaders(404,0);
            exchange.close();
            return;
        }
        try {
            int id = Integer.parseInt(fragments[3]);
            Optional<Product> productOptional = productService.getProductById(id);
            if (!productOptional.isPresent()){
                exchange.sendResponseHeaders(404,0);
                exchange.close();
                return;
            }

            productService.deleteProduct(id);
            exchange.sendResponseHeaders(204, 0);
            exchange.close();

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] responseErrorBody(String error) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> errorMap = Map.of("error", error);

        return  mapper.writeValueAsBytes(errorMap);

    }

    private String validateProduct(Product product, String type){
        if (product.getId() != null && type.equals("update")){
            Optional<Product> productOptional = productService.getProductById(product.getId());
            if (!productOptional.isPresent()){
                return "Product with that id does not exist";
            }
        }

        if (product.getName() != null && type.equals("add")){
            Optional<Product> productOptional = productService.getProductByName(product.getName());
            if (productOptional.isPresent()){
                return "Product with that name already exists";
            }
        }


        return null;
    }
}
