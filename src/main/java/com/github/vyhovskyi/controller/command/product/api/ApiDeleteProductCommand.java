package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Optional;

public class ApiDeleteProductCommand implements Command {
    private ProductService productService;

    public ApiDeleteProductCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        String[] segments = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(segments[segments.length - 1]);

        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            try{
                productService.deleteProduct(id);
                exchange.sendResponseHeaders(204, 0);
                exchange.close();
            }catch (ServiceException e) {
                HttpSender.sendJson(exchange, 500, "Server Error");
            }
            return;
        }
        exchange.sendResponseHeaders(404, 0);
        exchange.close();


    }
}
