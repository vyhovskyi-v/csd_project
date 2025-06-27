package com.github.vyhovskyi.controller.command.group.api;

import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.service.GroupService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.URI;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ApiDeleteGroupCommandTest {

    private GroupService groupService;
    private ApiDeleteGroupCommand command;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        command = new ApiDeleteGroupCommand(groupService);
        exchange = mock(HttpExchange.class);

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));
    }

    @Test
    void testExecute_DeleteSuccessful() throws Exception {
        int groupId = 5;

        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/" + groupId));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(Group.builder().id(groupId).build()));

        command.execute(exchange);

        verify(groupService, times(1)).deleteGroup(groupId);
        verify(exchange).sendResponseHeaders(204, 0);
        verify(exchange).close();
    }

    @Test
    void testExecute_GroupNotFound() throws Exception {
        int groupId = 5;

        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/" + groupId));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.empty());

        command.execute(exchange);

        verify(groupService, never()).deleteGroup(anyInt());
        verify(exchange).sendResponseHeaders(404, 0);
        verify(exchange).close();
    }

    @Test
    void testExecute_ServerError() throws Exception {
        int groupId = 5;

        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/" + groupId));
        when(groupService.getGroupById(groupId)).thenReturn(Optional.of(Group.builder().id(groupId).build()));
        doThrow(new RuntimeException("Simulated")).when(groupService).deleteGroup(groupId);

        command.execute(exchange);

        verify(exchange).sendResponseHeaders(500, 0);
        verify(exchange).close();
    }
}
