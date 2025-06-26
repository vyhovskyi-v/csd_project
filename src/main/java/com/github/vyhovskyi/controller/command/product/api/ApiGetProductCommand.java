package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Optional;

public class ApiGetProductCommand implements Command {
    ProductService productService;

    public ApiGetProductCommand(ProductService productService) {
        this.productService = productService;
    }
    @Override
    public void execute(HttpExchange exchange) throws IOException {
        String[] segments = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(segments[segments.length - 1]);

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            HttpSender.sendJson(exchange,200, product);
            return;
        }
        HttpSender.sendJson(exchange, 404, "Not Found");

    }
}
