package com.github.vyhovskyi.controller.command.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.entity.AmountRequest;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class ApiIncreaseQuantityProduct implements Command {
    private ProductService productService;
    public ApiIncreaseQuantityProduct(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String[] segments = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(segments[segments.length - 2]);
        System.out.println(id);
        AmountRequest request = mapper.readValue(exchange.getRequestBody(), AmountRequest.class);
        int amount = request.getAmount();
        System.out.println(amount);

        try{
            productService.increaseStock(id, amount);
            exchange.sendResponseHeaders(204, 0);
            exchange.close();
        }catch(ServiceException e){
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }
    }
}
