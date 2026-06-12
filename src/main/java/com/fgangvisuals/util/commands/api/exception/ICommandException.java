package com.fgangvisuals.util.commands.api.exception;

import net.minecraft.util.Formatting;
import com.fgangvisuals.util.QuickLogger;
import com.fgangvisuals.util.commands.api.ICommand;
import com.fgangvisuals.util.commands.api.argument.ICommandArgument;

import java.util.List;

public interface ICommandException extends QuickLogger {

    String getMessage();

    default void handle(ICommand command, List<ICommandArgument> args) {
        logDirect(
                this.getMessage(),
                Formatting.RED
        );
    }
}
