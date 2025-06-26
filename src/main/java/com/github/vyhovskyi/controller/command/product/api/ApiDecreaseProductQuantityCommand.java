package com.github.vyhovskyi.controller.command.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.AmountRequest;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiDecreaseProductQuantityCommand implements Command {
    private ProductService productService;
    public ApiDecreaseProductQuantityCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String[] segments = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(segments[segments.length - 2]);

        AmountRequest request = mapper.readValue(exchange.getRequestBody(), AmountRequest.class);
        int amount = request.getAmount();

        int quantityInStock = productService.getProductById(id).get().getQuantity();
        if (quantityInStock < amount) {
            List<String> errors = new ArrayList<>();
            errors.add("Not enough stock");

            HttpSender.sendErrors(exchange, 400, errors);
            return;
        }

        try{
            productService.decreaseStock(id, amount);
            exchange.sendResponseHeaders(204, 0);
            exchange.close();
        }catch(ServiceException e){
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }
    }
}
