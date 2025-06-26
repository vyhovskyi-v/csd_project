package com.github.vyhovskyi.controller.command.login;

import com.github.vyhovskyi.constans.Page;
import com.github.vyhovskyi.controller.command.Command;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class GetLoginCommand implements Command {

    @Override
    public void execute(HttpExchange exchange) throws IOException {
        HttpSender.sendHtmlPage(exchange, Page.LOGIN_VIEW);
    }
}
