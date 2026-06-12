package com.fgangvisuals.util.commands.api.datatypes;

import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.util.commands.api.exception.CommandException;
import com.fgangvisuals.util.commands.api.helpers.TabCompleteHelper;
import com.fgangvisuals.util.keyboard.KeyStorage;
import com.fgangvisuals.util.macro.Macro;

import java.util.List;
import java.util.stream.Stream;

public enum MacroDataType implements IDatatypeFor<Macro> {
    INSTANCE;

    @Override
    public Stream<String> tabComplete(IDatatypeContext datatypeContext) throws CommandException {
        Stream<String> macros = getMacro()
                .stream()
                .map(macro -> KeyStorage.getKey(macro.key()));

        String context = datatypeContext
                .getConsumer()
                .getString();

        return new TabCompleteHelper()
                .append(macros)
                .filterPrefix(context)
                .sortAlphabetically()
                .stream();
    }

    @Override
    public Macro get(IDatatypeContext datatypeContext) throws CommandException {
        String username = datatypeContext
                .getConsumer()
                .getString();

        return getMacro().stream()
                .filter(s -> KeyStorage.getKey(s.key()).equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    private List<? extends Macro> getMacro() {
        return FGANGVisuals.getInstance().getMacroRepository().getMacroList();
    }
}