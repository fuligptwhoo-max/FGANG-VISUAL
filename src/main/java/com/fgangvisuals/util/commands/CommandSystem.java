package com.fgangvisuals.util.commands;

import com.fgangvisuals.util.commands.api.ICommandSystem;
import com.fgangvisuals.util.commands.api.argparser.IArgParserManager;
import com.fgangvisuals.util.commands.argparser.ArgParserManager;

public enum CommandSystem implements ICommandSystem {
    INSTANCE;

    @Override
    public IArgParserManager getParserManager() {
        return ArgParserManager.INSTANCE;
    }
}
