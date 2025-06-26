package com.github.vyhovskyi.controller.command;

import com.github.vyhovskyi.constans.Page;
import com.github.vyhovskyi.controller.utils.HttpSender;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PageNotFoundCommand implements Command {
    @Override
    public void execute(HttpExchange exchange) throws IOException {
        HttpSender.sendHtmlPage(exchange, Page.PAGE_NOT_FOUND_VIEW);
    }
}
