package com.github.vyhovskyi.controller.command.group.api;

import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.GroupService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Optional;

public class ApiDeleteGroupCommand implements Command {
    GroupService groupService;
    public ApiDeleteGroupCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        String[] segments = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(segments[segments.length - 1]);

        Optional<Group> groupOptional = groupService.getGroupById(id);
        if (groupOptional.isPresent()) {
            try{
                groupService.deleteGroup(id);
                exchange.sendResponseHeaders(204, 0);
                exchange.close();
            }catch (Exception e) {
                exchange.sendResponseHeaders(500,0);
                exchange.close();
            }
            return;
        }

        exchange.sendResponseHeaders(404,0);
        exchange.close();
    }
}
