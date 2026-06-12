package com.fgangvisuals.util.commands.api.datatypes;

import com.fgangvisuals.util.commands.api.exception.CommandException;

public interface IDatatypeFor<T> extends IDatatype  {
    T get(IDatatypeContext datatypeContext) throws CommandException;
}
