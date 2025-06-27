package com.github.vyhovskyi.controller.command.group.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.service.GroupService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApiUpdateGroupCommandTest {

    private GroupService groupService;
    private ApiUpdateGroupCommand command;
    private HttpExchange exchange;
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() {
        groupService = mock(GroupService.class);
        command = new ApiUpdateGroupCommand(groupService);
        exchange = mock(HttpExchange.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testExecute_Success() throws Exception {
        Group group = Group.builder().id(1).name("Test").description("Desc").build();
        String json = mapper.writeValueAsString(group);

        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/1"));

        when(groupService.getGroupById(1)).thenReturn(Optional.of(Group.builder().id(1).name("OldName").build()));

        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);

        OutputStream os = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(os);

        command.execute(exchange);

        verify(groupService).updateGroup(any(Group.class));
        verify(exchange).sendResponseHeaders(eq(204), eq(0L));
    }

    @Test
    public void testExecute_GroupNotFound() throws Exception {
        Group group = Group.builder().id(1).name("Test").description("Desc").build();
        String json = mapper.writeValueAsString(group);

        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/1"));
        when(groupService.getGroupById(1)).thenReturn(Optional.empty());

        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        OutputStream os = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(os);

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(400), anyLong());
    }

    @Test
    public void testExecute_NameAlreadyExists() throws Exception {
        Group group = Group.builder().id(1).name("NewName").description("Desc").build();
        String json = mapper.writeValueAsString(group);

        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(json.getBytes()));
        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/1"));

        when(groupService.getGroupById(1)).thenReturn(Optional.of(Group.builder().id(1).name("OldName").build()));
        when(groupService.getGroupByName("NewName")).thenReturn(Optional.of(Group.builder().id(2).build()));

        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        OutputStream os = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(os);

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(eq(400), anyLong());
    }
}