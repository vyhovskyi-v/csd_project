package com.github.vyhovskyi.controller.command.group.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.exception.ServiceException;
import com.github.vyhovskyi.service.GroupService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApiUpdateGroupCommand implements Command {
    GroupService groupService;

    public ApiUpdateGroupCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Group group = mapper.readValue(exchange.getRequestBody(), Group.class);

        String[] segments = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(segments[segments.length - 1]);
        group.setId(id);

        List<String> errors = validate(group);
        if (!errors.isEmpty()) {
            HttpSender.sendErrors(exchange, 400, errors);
            return;
        }

        try{
            groupService.updateGroup(group);
            exchange.sendResponseHeaders(204, 0);
            exchange.close();
        }catch (ServiceException e){
            exchange.sendResponseHeaders(500,0);
            exchange.close();
        }
    }

    private List<String> validate(Group group) {
        List<String> errors = new ArrayList<>();

        Optional<Group> existingGroup = groupService.getGroupById(group.getId());
        if (existingGroup.isEmpty()) {
            errors.add("Group with such id does not exist");
            return errors; // немає сенсу далі перевіряти
        }

        String oldName = existingGroup.get().getName();

        if (group.getName() == null || group.getName().isBlank()) {
            errors.add("Group name cannot be blank");
        } else if (!oldName.equals(group.getName())) {
            Optional<Group> groupOptional = groupService.getGroupByName(group.getName());
            if (groupOptional.isPresent()) {
                errors.add("Group with such name already exist");
            }
        }

        if (group.getDescription() == null || group.getDescription().isBlank()) {
            errors.add("Group description cannot be blank");
        }

        return errors;
    }
}
