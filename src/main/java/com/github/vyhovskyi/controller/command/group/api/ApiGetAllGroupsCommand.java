package com.github.vyhovskyi.controller.command.group.api;

import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.ProductFilter;
import com.github.vyhovskyi.service.GroupService;
import com.sun.net.httpserver.HttpExchange;
import com.github.vyhovskyi.entity.Group;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiGetAllGroupsCommand implements Command {
    private GroupService groupService;

    public ApiGetAllGroupsCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        Map<String, String> params = getQueryParams(exchange);

        String name = null;
        String description = null;
        if (params.containsKey("name")) {
            name = params.get("name");
        }
        if (params.containsKey("description")) {
            description = params.get("description");
        }

        List<Group> groups = groupService.getGroupsByFilter(name, description);

        HttpSender.sendJson(exchange, 200, groups);

    }


    private static Map<String, String> getQueryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2) {
                    String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                    params.put(key, value);
                }
            }
        }

        System.out.println(params);
        return params;
    }
}
