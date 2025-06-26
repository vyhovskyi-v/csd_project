package com.github.vyhovskyi.controller.command.group.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.GroupService;
import com.github.vyhovskyi.service.ProductService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApiCreateGroupCommand implements Command {
    GroupService groupService;

    public ApiCreateGroupCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Group group = mapper.readValue(exchange.getRequestBody(), Group.class);

        List<String> errors = validate(group);
        if (!errors.isEmpty()) {
            HttpSender.sendErrors(exchange, 400, errors);
            return;
        }

        try{
            groupService.addGroup(group);
            exchange.sendResponseHeaders(204, 0);
            exchange.close();
        }catch (ServiceException e){
            exchange.sendResponseHeaders(500,0);
            exchange.close();
        }
    }

    private List<String> validate(Group group) {
        List<String> errors = new ArrayList<>();

        if (group.getName() == null || group.getName().isBlank()) {
            errors.add("Group name cannot be blank");

            Optional<Group> groupOptional = groupService.getGroupByName(group.getName());
            if (groupOptional.isPresent()) {
                errors.add("Group with the same name already exists");
            }
        }

        if (group.getDescription() == null || group.getDescription().isBlank()) {
            errors.add("Group description cannot be blank");
        }

        return errors;
    }
}
