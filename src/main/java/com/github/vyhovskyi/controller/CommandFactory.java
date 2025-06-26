package com.github.vyhovskyi.controller;

import com.github.vyhovskyi.controller.command.Command;

public class CommandFactory {
    private CommandFactory() {}

    static Command getCommand(String commandKey) {
        Command command = CommandEnum.getCommand(commandKey);
        return command;
    }
}
