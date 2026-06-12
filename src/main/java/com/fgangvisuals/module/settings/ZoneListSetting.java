package com.fgangvisuals.module.settings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fgangvisuals.module.list.misc.ZoneData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ZoneListSetting extends Setting {

    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<List<ZoneData>>() {}.getType();

    private List<ZoneData> value = new ArrayList<>();

    public ZoneListSetting(String name) {
        super(name);
    }

    public List<ZoneData> getValue() {
        return value;
    }

    public void setValue(List<ZoneData> value) {
        this.value = value != null ? value : new ArrayList<>();
        triggerChange();
    }

    public void add(ZoneData zone) {
        value.add(zone);
        triggerChange();
    }

    public ZoneData remove(int index) {
        if (index < 0 || index >= value.size()) return null;
        ZoneData removed = value.remove(index);
        triggerChange();
        return removed;
    }

    public int size() {
        return value.size();
    }

    public ZoneData get(int index) {
        if (index < 0 || index >= value.size()) return null;
        return value.get(index);
    }

    @Override
    public String getValueAsString() {
        return gson.toJson(value);
    }

    @Override
    public void setValueFromString(String str) {
        List<ZoneData> parsed = gson.fromJson(str, type);
        value = parsed != null ? parsed : new ArrayList<>();
    }

    @Override
    public ZoneListSetting setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
        return this;
    }
}
