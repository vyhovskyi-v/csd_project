package com.github.vyhovskyi.controller.command.group.api;

import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.service.GroupService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class
ApiGetAllGroupsCommandTest {

    private GroupService groupService;
    private ApiGetAllGroupsCommand command;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        command = new ApiGetAllGroupsCommand(groupService);
        exchange = mock(HttpExchange.class);

        when(exchange.getResponseHeaders()).thenReturn(new Headers());
        when(exchange.getResponseBody()).thenReturn(mock(OutputStream.class));
    }

    @Test
    void testExecute_WithParams() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups?name=food&description=products"));
        when(groupService.getGroupsByFilter("food", "products")).thenReturn(Collections.singletonList(
                Group.builder().id(1).name("food").description("products").build()
        ));

        command.execute(exchange);

        verify(groupService, times(1)).getGroupsByFilter("food", "products");
        verify(exchange).sendResponseHeaders(eq(200), anyLong());
    }

    @Test
    void testExecute_WithoutParams() throws Exception {
        when(exchange.getRequestURI()).thenReturn(new URI("/api/groups"));
        when(groupService.getGroupsByFilter(null, null)).thenReturn(List.of());

        command.execute(exchange);

        verify(groupService, times(1)).getGroupsByFilter(null, null);
        verify(exchange).sendResponseHeaders(eq(200), anyLong());
    }
}
