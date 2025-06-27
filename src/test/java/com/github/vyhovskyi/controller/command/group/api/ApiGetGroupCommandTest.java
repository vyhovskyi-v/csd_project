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

class ApiGetGroupCommandTest {

    private GroupService groupService;
    private ApiGetGroupCommand command;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        command = new ApiGetGroupCommand(groupService);
        exchange = mock(HttpExchange.class);

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));
    }

    @Test
    void testExecute_GroupExists() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/5"));
        Group group = Group.builder().id(5).name("Food").description("Products").build();
        when(groupService.getGroupById(5)).thenReturn(Optional.of(group));

        command.execute(exchange);

        verify(groupService, times(1)).getGroupById(5);
        verify(exchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    void testExecute_GroupNotFound() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups/10"));
        when(groupService.getGroupById(10)).thenReturn(Optional.empty());

        command.execute(exchange);

        verify(groupService, times(1)).getGroupById(10);
        verify(exchange).sendResponseHeaders(eq(404), eq(0L));
        verify(exchange).close();
    }
}
