package com.fgangvisuals.module.settings;

import java.util.function.Supplier;

public abstract class Setting implements ISetting {
    private final String name;
    public Supplier<Boolean> visible = () -> true;
    protected Runnable onChange;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    protected void triggerChange() {
        if (onChange != null) onChange.run();
    }

    public abstract String getValueAsString();
    public abstract void setValueFromString(String value);
}