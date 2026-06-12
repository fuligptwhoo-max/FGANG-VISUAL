package com.fgangvisuals.module.settings;

import lombok.Getter;
import lombok.Setter;
import com.fgangvisuals.util.render.math.Animation;
import com.fgangvisuals.util.render.math.Easing;

import java.util.function.Supplier;

@Getter
@Setter
public class BooleanSetting extends Setting {
    private final Animation animation = new Animation(Easing.QUINTIC_OUT, 400);
    private final Animation clickAnimation = new Animation(Easing.QUINTIC_OUT, 400);
    private boolean clicked;
    private boolean value;
    private int key = -1;

    public BooleanSetting(String name, boolean defaultValue) {
        super(name);
        this.value = defaultValue;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
        triggerChange();
    }

    @Override
    public String getValueAsString() {
        return Boolean.toString(value);
    }

    @Override
    public void setValueFromString(String value) {
        setValue(Boolean.parseBoolean(value));
    }

    public void toggle() {
        setValue(!this.value);
    }

    @Override
    public BooleanSetting setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
        return this;
    }
}