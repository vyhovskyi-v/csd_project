package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.OutputStream;
import java.net.URI;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ApiDeleteProductCommandTest {

    private ProductService productService;
    private ApiDeleteProductCommand command;
    private HttpExchange exchange;

    @BeforeEach
    public void setUp() {
        productService = mock(ProductService.class);
        command = new ApiDeleteProductCommand(productService);
        exchange = mock(HttpExchange.class);

        // Пустий заглушка Headers, щоб уникнути NPE
        when(exchange.getResponseHeaders()).thenReturn(mock(Headers.class));
    }

    @Test
    public void testExecute_ProductExists_DeletedSuccessfully() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.of(new Product()));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(productService, times(1)).deleteProduct(5);
        verify(exchange, times(1)).sendResponseHeaders(204, 0);
        verify(exchange, times(1)).close();
    }

    @Test
    public void testExecute_ProductDoesNotExist_Returns404() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.empty());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange, times(1)).sendResponseHeaders(404, 0);
        verify(exchange, times(1)).close();
    }

    @Test
    public void testExecute_ServiceException_Returns500() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.of(new Product()));
        doThrow(new ServiceException("Test")).when(productService).deleteProduct(5);
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(productService, times(1)).deleteProduct(5);
        verify(exchange, times(1)).sendResponseHeaders(eq(500), anyLong());
    }
}
