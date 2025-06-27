package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ApiGetProductCommandTest {

    private ProductService productService;
    private HttpExchange exchange;
    private ApiGetProductCommand command;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        exchange = mock(HttpExchange.class);
        command = new ApiGetProductCommand(productService);

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
    }

    @Test
    void testExecute_ProductFound() throws Exception {
        Product product = Product.builder()
                .id(5)
                .name("Test Product")
                .description("Test Desc")
                .manufacturerId(2)
                .quantity(50)
                .price(new BigDecimal("199.99"))
                .group(null)
                .build();

        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.of(product));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    void testExecute_ProductNotFound() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/100"));
        when(productService.getProductById(100)).thenReturn(Optional.empty());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(404), anyLong());
    }
}
