package com.fgangvisuals.event.list;

import com.fgangvisuals.event.Event;

public class TabCompleteEvent extends Event {
    private final String prefix;
    public String[] completions;

    public TabCompleteEvent(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setCompletions(String[] completions) {
        this.completions = completions;
    }
}