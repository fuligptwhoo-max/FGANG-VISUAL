package com.fgangvisuals.util.commands.api.manager;

import net.minecraft.util.Pair;
import com.fgangvisuals.util.commands.api.ICommand;
import com.fgangvisuals.util.commands.api.argument.ICommandArgument;
import com.fgangvisuals.util.commands.api.registry.Registry;

import java.util.List;
import java.util.stream.Stream;

public interface ICommandManager {
    Registry<ICommand> getRegistry();

    ICommand getCommand(String name);

    boolean execute(String string);

    boolean execute(Pair<String, List<ICommandArgument>> expanded);

    Stream<String> tabComplete(Pair<String, List<ICommandArgument>> expanded);

    Stream<String> tabComplete(String prefix);
}
