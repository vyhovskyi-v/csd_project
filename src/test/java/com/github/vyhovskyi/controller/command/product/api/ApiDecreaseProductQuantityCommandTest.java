package com.github.vyhovskyi.controller.command.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.entity.AmountRequest;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ApiDecreaseProductQuantityCommandTest {

    private ProductService productService;
    private ApiDecreaseProductQuantityCommand command;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        command = new ApiDecreaseProductQuantityCommand(productService);
    }

    @Test
    void testExecute_SuccessfulDecrease() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();

        String json = "{\"amount\": 2}";
        InputStream requestBody = new ByteArrayInputStream(json.getBytes());

        when(exchange.getRequestBody()).thenReturn(requestBody);
        when(exchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestURI()).thenReturn(URI.create("/api/products/5/decrease"));

        Product product = Product.builder()
                .id(5)
                .name("Test Product")
                .quantity(10)
                .price(BigDecimal.TEN)
                .build();

        when(productService.getProductById(5)).thenReturn(Optional.of(product));

        command.execute(exchange);

        verify(productService).decreaseStock(5, 2);
        verify(exchange).sendResponseHeaders(204, 0);
        verify(exchange).close();
    }

    @Test
    void testExecute_NotEnoughStock() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();

        String json = "{\"amount\": 15}";
        InputStream requestBody = new ByteArrayInputStream(json.getBytes());
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

        when(exchange.getRequestBody()).thenReturn(requestBody);
        when(exchange.getResponseBody()).thenReturn(responseBody);
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestURI()).thenReturn(URI.create("/api/products/7/decrease"));

        Product product = Product.builder()
                .id(7)
                .name("Test Product")
                .quantity(5)
                .price(BigDecimal.TEN)
                .build();

        when(productService.getProductById(7)).thenReturn(Optional.of(product));

        command.execute(exchange);

        String response = responseBody.toString();
        assert response.contains("Not enough stock");

        verify(exchange).sendResponseHeaders(400, response.length());
    }
}
