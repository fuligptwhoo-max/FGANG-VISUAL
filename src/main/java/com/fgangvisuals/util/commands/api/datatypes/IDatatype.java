package com.fgangvisuals.util.commands.api.datatypes;

import com.fgangvisuals.util.IMinecraft;
import com.fgangvisuals.util.commands.api.exception.CommandException;

import java.util.stream.Stream;

public interface IDatatype extends IMinecraft {
    Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException;
}
