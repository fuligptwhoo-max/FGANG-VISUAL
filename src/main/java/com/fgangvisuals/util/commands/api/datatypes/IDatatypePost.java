package com.fgangvisuals.util.commands.api.datatypes;

import com.fgangvisuals.util.commands.api.exception.CommandException;

public interface IDatatypePost<T, O> extends IDatatype {
    T apply(IDatatypeContext datatypeContext, O original) throws CommandException;
}
