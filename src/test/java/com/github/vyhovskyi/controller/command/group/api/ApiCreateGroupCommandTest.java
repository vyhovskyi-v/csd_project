package com.github.vyhovskyi.controller.command.group.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.service.GroupService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ApiCreateGroupCommandTest {

    private GroupService groupService;
    private ApiCreateGroupCommand command;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        command = new ApiCreateGroupCommand(groupService);
        exchange = mock(HttpExchange.class);

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
    }

    @Test
    void testExecute_SuccessfulCreation() throws Exception {
        Group group = Group.builder()
                .name("Electronics")
                .description("Electronic devices")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(mapper.writeValueAsBytes(group)));
        when(groupService.getGroupByName("Electronics")).thenReturn(Optional.empty());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(groupService, times(1)).addGroup(any(Group.class));
        verify(exchange).sendResponseHeaders(204, 0);
    }

    @Test
    void testExecute_GroupNameBlank() throws Exception {
        Group group = Group.builder()
                .name(" ")
                .description("Test description")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(mapper.writeValueAsBytes(group)));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    void testExecute_GroupWithSameNameExists() throws Exception {
        Group group = Group.builder()
                .name("Electronics")
                .description("Devices")
                .build();

        when(groupService.getGroupByName("Electronics")).thenReturn(Optional.of(group));

        ObjectMapper mapper = new ObjectMapper();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(mapper.writeValueAsBytes(group)));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    void testExecute_GroupDescriptionBlank() throws Exception {
        Group group = Group.builder()
                .name("Electronics")
                .description(" ")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(mapper.writeValueAsBytes(group)));
        when(groupService.getGroupByName("Electronics")).thenReturn(Optional.empty());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    void testExecute_ServiceException() throws Exception {
        Group group = Group.builder()
                .name("Electronics")
                .description("Devices")
                .build();

        when(groupService.getGroupByName("Electronics")).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Simulated")).when(groupService).addGroup(any(Group.class));

        ObjectMapper mapper = new ObjectMapper();
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(mapper.writeValueAsBytes(group)));
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(500), eq(0L));
    }
}
