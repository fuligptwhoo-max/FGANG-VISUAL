package com.fgangvisuals.util.commands.api;

import com.fgangvisuals.util.commands.api.argparser.IArgParserManager;

public interface ICommandSystem {
    IArgParserManager getParserManager();
}
