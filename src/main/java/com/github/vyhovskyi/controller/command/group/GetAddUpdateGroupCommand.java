package com.github.vyhovskyi.controller.command.group;

import com.github.vyhovskyi.constans.Page;
import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class GetAddUpdateGroupCommand implements Command {

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        HttpSender.sendHtmlPage(exchange, Page.ADD_UPDATE_GROUP_VIEW);
    }
}
