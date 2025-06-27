package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class ApiGetAllProductsCommandTest {

    private ProductService productService;
    private ApiGetAllProductsCommand command;
    private HttpExchange exchange;

    @BeforeEach
    public void setUp() {
        productService = mock(ProductService.class);
        command = new ApiGetAllProductsCommand(productService);
        exchange = mock(HttpExchange.class);

        // Заглушка для уникнення NPE
        when(exchange.getResponseHeaders()).thenReturn(mock(Headers.class));
    }

    @Test
    public void testExecute_Successful() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/products?name=Beer"));
        when(productService.getProductsByFilter(any(), eq(0), eq(0))).thenReturn(Collections.singletonList(new Product()));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));


        command.execute(exchange);

        verify(productService, times(1)).getProductsByFilter(any(), eq(0), eq(0));
    }

    @Test
    public void testExecute_ServerError() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/products"));
        when(productService.getProductsByFilter(any(), eq(0), eq(0))).thenThrow(new RuntimeException("Simulated"));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange, times(1)).sendResponseHeaders(500, 0);
    }
}
