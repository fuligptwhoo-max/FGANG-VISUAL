package com.fgangvisuals.util.commands.api.exception;

import com.fgangvisuals.util.QuickLogger;
import com.fgangvisuals.util.commands.api.ICommand;
import com.fgangvisuals.util.commands.api.argument.ICommandArgument;

import java.util.List;

public class CommandUnhandledException extends RuntimeException implements ICommandException, QuickLogger {

    public CommandUnhandledException(String message) {
        super(message);
    }

    public CommandUnhandledException(Throwable cause) {
        super(cause);
    }

    @Override
    public void handle(ICommand command, List<ICommandArgument> args) {
    }
}
