package com.fgangvisuals.module.settings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fgangvisuals.module.list.player.ServerHelperItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemListSetting extends Setting {

    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<List<ServerHelperItem>>() {}.getType();

    private List<ServerHelperItem> value = new ArrayList<>();

    public ItemListSetting(String name) {
        super(name);
    }

    public List<ServerHelperItem> getValue() {
        return value;
    }

    public void setValue(List<ServerHelperItem> value) {
        this.value = value != null ? value : new ArrayList<>();
        triggerChange();
    }

    public void add(ServerHelperItem item) {
        value.add(item);
        triggerChange();
    }

    public ServerHelperItem remove(int index) {
        if (index < 0 || index >= value.size()) return null;
        ServerHelperItem removed = value.remove(index);
        triggerChange();
        return removed;
    }

    public void clear() {
        value.clear();
        triggerChange();
    }

    public int size() {
        return value.size();
    }

    public ServerHelperItem get(int index) {
        if (index < 0 || index >= value.size()) return null;
        return value.get(index);
    }

    @Override
    public String getValueAsString() {
        return gson.toJson(value);
    }

    @Override
    public void setValueFromString(String str) {
        List<ServerHelperItem> parsed = gson.fromJson(str, type);
        value = parsed != null ? parsed : new ArrayList<>();
    }

    @Override
    public ItemListSetting setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
        return this;
    }
}
