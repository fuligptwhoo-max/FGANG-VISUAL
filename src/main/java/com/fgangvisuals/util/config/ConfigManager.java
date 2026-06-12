package com.fgangvisuals.util.config;

import com.google.gson.*;
import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.list.misc.ZoneData;
import com.fgangvisuals.module.list.player.ServerHelperItem;
import com.fgangvisuals.module.settings.*;
import com.fgangvisuals.module.settings.impl.Theme;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FOLDER = Paths.get("fgangvisuals/configs");
    public static boolean isLoading;
    private static long lastAutoSave;
    private static final long AUTO_SAVE_INTERVAL = 1000;

    public static void autoSave() {
        long now = System.currentTimeMillis();
        if (now - lastAutoSave >= AUTO_SAVE_INTERVAL) {
            lastAutoSave = now;
            save("autocfg");
        }
    }

    public static void save(String name) {
        JsonObject root = new JsonObject();

        for (Module module : FGANGVisuals.getInstance().getModuleStorage().getModules()) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enabled", module.isEnabled());
            moduleObject.addProperty("keybind", module.getKey());

            JsonObject settingsObject = new JsonObject();
            for (Setting setting : module.getSettings()) {
                if (setting instanceof BooleanSetting s) {
                    settingsObject.addProperty(setting.getName(), s.getValue());
                } else if (setting instanceof BindSetting s) {
                    settingsObject.addProperty(setting.getName(), s.getValue());
                } else if (setting instanceof ModeSetting s) {
                    settingsObject.addProperty(setting.getName(), s.getValue());
                } else if (setting instanceof SliderSetting s) {
                    settingsObject.addProperty(setting.getName(), s.getValue());
                } else if (setting instanceof ThemeSetting s) {
                    settingsObject.addProperty(setting.getName(), s.getValue().name);
                } else if (setting instanceof ModeListSetting s) {
                    JsonArray enabledModes = new JsonArray();
                    for (String name2 : s.getEnabledModules()) {
                        enabledModes.add(name2);
                    }
                    settingsObject.add(setting.getName(), enabledModes);
                } else if (setting instanceof ColorSetting s) {
                    settingsObject.addProperty(setting.getName(), s.getValue());
                } else if (setting instanceof ItemListSetting s) {
                    JsonArray arr = new JsonArray();
                    for (ServerHelperItem item : s.getValue()) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("itemId", item.itemId);
                        obj.addProperty("key", item.key);
                        arr.add(obj);
                    }
                    settingsObject.add(setting.getName(), arr);
                } else if (setting instanceof ZoneListSetting s) {
                    JsonArray arr = new JsonArray();
                    for (ZoneData zone : s.getValue()) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("warp", zone.warpCommand);
                        obj.addProperty("minX", zone.minX);
                        obj.addProperty("minZ", zone.minZ);
                        obj.addProperty("maxX", zone.maxX);
                        obj.addProperty("maxZ", zone.maxZ);
                        arr.add(obj);
                    }
                    settingsObject.add(setting.getName(), arr);
                }
            }

            moduleObject.add("settings", settingsObject);
            root.add(module.getName(), moduleObject);
        }

        try {
            Files.createDirectories(CONFIG_FOLDER);
            Path configFile = CONFIG_FOLDER.resolve(name + ".json");
            Files.write(configFile, gson.toJson(root).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String name) {
        Path configFile = CONFIG_FOLDER.resolve(name + ".json");
        if (!Files.exists(configFile)) return;

        isLoading = true;

        try (Reader reader = Files.newBufferedReader(configFile)) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            for (Module module : FGANGVisuals.getInstance().getModuleStorage().getModules()) {
                if (!root.has(module.getName())) continue;
                JsonObject moduleObject = root.getAsJsonObject(module.getName());

                if (moduleObject.has("enabled")) {
                    boolean enabled = moduleObject.get("enabled").getAsBoolean();
                    module.setEnabled(enabled);
                }
                if (moduleObject.has("keybind")) {
                    int keybind = moduleObject.get("keybind").getAsInt();
                    module.setKey(keybind);
                }
                if (moduleObject.has("settings")) {
                    JsonObject settingsObject = moduleObject.getAsJsonObject("settings");
                    for (Setting setting : module.getSettings()) {
                        if (!settingsObject.has(setting.getName())) continue;

                        JsonElement element = settingsObject.get(setting.getName());
                        if (setting instanceof BooleanSetting s) {
                            s.setValue(element.getAsBoolean());
                        } else if (setting instanceof BindSetting s) {
                            s.setValue(element.getAsInt());
                        } else if (setting instanceof ModeSetting s) {
                            s.setValue(element.getAsString());
                        } else if (setting instanceof SliderSetting s) {
                            s.setValue(element.getAsDouble());
                        } else if (setting instanceof ThemeSetting s) {
                            String themeName = element.getAsString();
                            for (Theme theme : s.getThemes()) {
                                if (theme.name.equals(themeName)) {
                                    s.setValue(theme);
                                    break;
                                }
                            }
                        } else if (setting instanceof ModeListSetting s && element.isJsonArray()) {
                            JsonArray array = element.getAsJsonArray();
                            List<String> enabled = new ArrayList<>();
                            for (JsonElement e : array) {
                                enabled.add(e.getAsString());
                            }
                            for (BooleanSetting subSetting : s.getSettings()) {
                                subSetting.setValue(enabled.contains(subSetting.getName()));
                            }
                        } else if (setting instanceof ColorSetting s) {
                            s.setValue(element.getAsInt());
                        } else if (setting instanceof ItemListSetting s && element.isJsonArray()) {
                            JsonArray arr = element.getAsJsonArray();
                            List<ServerHelperItem> items = new ArrayList<>();
                            for (JsonElement e : arr) {
                                JsonObject obj = e.getAsJsonObject();
                                ServerHelperItem item = new ServerHelperItem();
                                item.itemId = obj.get("itemId").getAsString();
                                item.key = obj.get("key").getAsInt();
                                items.add(item);
                            }
                            s.setValue(items);
                        } else if (setting instanceof ZoneListSetting s && element.isJsonArray()) {
                            JsonArray arr = element.getAsJsonArray();
                            List<ZoneData> zones = new ArrayList<>();
                            for (JsonElement e : arr) {
                                JsonObject obj = e.getAsJsonObject();
                                ZoneData z = new ZoneData();
                                z.warpCommand = obj.get("warp").getAsString();
                                z.minX = obj.get("minX").getAsInt();
                                z.minZ = obj.get("minZ").getAsInt();
                                z.maxX = obj.get("maxX").getAsInt();
                                z.maxZ = obj.get("maxZ").getAsInt();
                                zones.add(z);
                            }
                            s.setValue(zones);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            isLoading = false;
        }
    }

    public static List<String> getConfigs() {
        List<String> configs = new ArrayList<>();
        try {
            if (Files.exists(CONFIG_FOLDER)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(CONFIG_FOLDER, "*.json")) {
                    for (Path path : stream) {
                        String fileName = path.getFileName().toString();
                        if (fileName.endsWith(".json")) {
                            configs.add(fileName.substring(0, fileName.length() - 5));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configs;
    }
}