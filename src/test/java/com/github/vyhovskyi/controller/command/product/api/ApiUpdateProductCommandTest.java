package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.service.GroupService;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiUpdateProductCommandTest {

    private ProductService productService;
    private GroupService groupService;
    private ApiUpdateProductCommand command;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        groupService = mock(GroupService.class);
        command = new ApiUpdateProductCommand(productService, groupService);
    }

    @Test
    void testExecute_ValidProduct() throws Exception {
        Product existingProduct = Product.builder().id(5).name("Old Name").build();
        Group group = Group.builder().id(1).name("Test Group").build();

        HttpExchange exchange = mock(HttpExchange.class);
        String json = "{" +
                "\"name\":\"New Name\"," +
                "\"description\":\"Desc\"," +
                "\"manufacturerId\":1," +
                "\"quantity\":10," +
                "\"price\":15.5," +
                "\"group\": {\"id\": 1}" +
                "}";

        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.of(existingProduct));
        when(productService.getProductByName("New Name")).thenReturn(Optional.empty());
        when(groupService.getGroupById(1)).thenReturn(Optional.of(group));

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(productService).updateProduct(any(Product.class));
        verify(exchange).sendResponseHeaders(204, 0);
    }

    @Test
    void testExecute_ProductWithSameNameExists() throws Exception {
        Product existingProduct = Product.builder().id(5).name("Old Name").build();
        Product conflictProduct = Product.builder().id(10).name("New Name").build();
        Group group = Group.builder().id(1).name("Test Group").build();

        HttpExchange exchange = mock(HttpExchange.class);
        String json = "{" +
                "\"name\":\"New Name\"," +
                "\"description\":\"Desc\"," +
                "\"manufacturerId\":1," +
                "\"quantity\":10," +
                "\"price\":15.5," +
                "\"group\": {\"id\": 1}" +
                "}";

        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.of(existingProduct));
        when(productService.getProductByName("New Name")).thenReturn(Optional.of(conflictProduct));
        when(groupService.getGroupById(1)).thenReturn(Optional.of(group));

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(409), anyLong());
    }

    @Test
    void testExecute_GroupDoesNotExist() throws Exception {
        Product existingProduct = Product.builder().id(5).name("Same Name").build();

        HttpExchange exchange = mock(HttpExchange.class);
        String json = "{" +
                "\"name\":\"Same Name\"," +
                "\"description\":\"Desc\"," +
                "\"manufacturerId\":1," +
                "\"quantity\":10," +
                "\"price\":15.5," +
                "\"group\": {\"id\": 99}" +
                "}";

        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.of(existingProduct));
        when(groupService.getGroupById(99)).thenReturn(Optional.empty());
        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(409), anyLong());
    }

    @Test
    void testExecute_ServiceException() throws Exception {
        Product existingProduct = Product.builder().id(5).name("Same Name").build();
        Group group = Group.builder().id(1).name("Test Group").build();

        HttpExchange exchange = mock(HttpExchange.class);
        String json = "{" +
                "\"name\":\"Same Name\"," +
                "\"description\":\"Desc\"," +
                "\"manufacturerId\":1," +
                "\"quantity\":10," +
                "\"price\":15.5," +
                "\"group\": {\"id\": 1}" +
                "}";

        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/api/products/5"));
        when(productService.getProductById(5)).thenReturn(Optional.of(existingProduct));
        when(groupService.getGroupById(1)).thenReturn(Optional.of(group));
        doThrow(new RuntimeException("Simulated")).when(productService).updateProduct(any());

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(500), anyLong());
    }
}
