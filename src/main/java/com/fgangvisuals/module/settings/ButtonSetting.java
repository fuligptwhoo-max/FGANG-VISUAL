package com.fgangvisuals.module.settings;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
public class ButtonSetting extends Setting {

    private final String buttonText;
    private final Runnable action;

    public ButtonSetting(String name, String buttonText, Runnable action) {
        super(name);
        this.buttonText = buttonText;
        this.action = action;
    }

    public void click() {
        if (action != null) action.run();
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public void setValueFromString(String value) {
        // No persistent value
    }

    @Override
    public ButtonSetting setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
        return this;
    }
}
