package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.entity.AmountRequest;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import static org.mockito.Mockito.*;

class ApiIncreaseQuantityProductTest {

    private ProductService productService;
    private HttpExchange exchange;
    private ApiIncreaseQuantityProduct command;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        exchange = mock(HttpExchange.class);
        command = new ApiIncreaseQuantityProduct(productService);

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
    }

    @Test
    void testExecute_Success() throws Exception {
        String requestBody = "{\"amount\": 10}";
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/5/increase"));
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(requestBody.getBytes()));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(productService).increaseStock(5, 10);
        verify(exchange).sendResponseHeaders(204, 0);
        verify(exchange).close();
    }

    @Test
    void testExecute_ServerError() throws Exception {
        String requestBody = "{\"amount\": 10}";
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/5/increase"));
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(requestBody.getBytes()));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        doThrow(new RuntimeException("Simulated")).when(productService).increaseStock(5, 10);

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(500, 0);
        verify(exchange).close();
    }
}
