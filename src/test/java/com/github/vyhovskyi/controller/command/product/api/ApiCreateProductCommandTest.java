package com.github.vyhovskyi.controller.command.product.api;

import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.entity.Product;
import com.github.vyhovskyi.service.GroupService;
import com.github.vyhovskyi.service.ProductService;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ApiCreateProductCommandTest {

    private ProductService productService;
    private GroupService groupService;
    private ApiCreateProductCommand command;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        groupService = mock(GroupService.class);
        command = new ApiCreateProductCommand(productService, groupService);
    }

    @Test
    void testExecute_ValidProduct() throws IOException {


        // Arrange
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);

        String jsonInput = """
            {
              "name": "Test Product",
              "description": "Description",
              "manufacturerId": 1,
              "quantity": 100,
              "price": 19.99,
              "group": { "id": 1 }
            }
            """;

        InputStream requestBody = new ByteArrayInputStream(jsonInput.getBytes());
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

        when(exchange.getRequestBody()).thenReturn(requestBody);
        when(exchange.getResponseBody()).thenReturn(responseBody);

        when(productService.getProductByName("Test Product")).thenReturn(Optional.empty());
        when(groupService.getGroupById(1)).thenReturn(Optional.of(Group.builder().id(1).name("Group").description("desc").build()));
        when(productService.createProduct(any())).thenReturn(10);

        // Act
        command.execute(exchange);

        // Assert
        String response = responseBody.toString();
        System.out.println(response);
        assert response.contains("\"id\":10");
    }

    @Test
    void testExecute_ProductAlreadyExists() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);

        String jsonInput = """
            {
              "name": "Existing Product",
              "description": "Description",
              "manufacturerId": 1,
              "quantity": 100,
              "price": 19.99,
              "group": { "id": 1 }
            }
            """;

        InputStream requestBody = new ByteArrayInputStream(jsonInput.getBytes());
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

        when(exchange.getRequestBody()).thenReturn(requestBody);
        when(exchange.getResponseBody()).thenReturn(responseBody);

        when(productService.getProductByName("Existing Product")).thenReturn(Optional.of(mock(Product.class)));

        // Act
        command.execute(exchange);

        String response = responseBody.toString();
        System.out.println(response);
        assert response.contains("Product with that name already exists");
    }

    @Test
    void testExecute_GroupDoesNotExist() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);

        String jsonInput = """
            {
              "name": "Test Product",
              "description": "Description",
              "manufacturerId": 1,
              "quantity": 100,
              "price": 19.99,
              "group": { "id": 99 }
            }
            """;

        InputStream requestBody = new ByteArrayInputStream(jsonInput.getBytes());
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

        when(exchange.getRequestBody()).thenReturn(requestBody);
        when(exchange.getResponseBody()).thenReturn(responseBody);

        when(productService.getProductByName("Test Product")).thenReturn(Optional.empty());
        when(groupService.getGroupById(99)).thenReturn(Optional.empty());

        // Act
        command.execute(exchange);

        String response = responseBody.toString();
        System.out.println(response);
        assert response.contains("Group with that id does not exist");
    }
}
