package com.fgangvisuals.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.module.list.render.ClientSounds;
import com.fgangvisuals.module.list.render.hud.Interface;
import com.fgangvisuals.module.settings.ModeSetting;
import com.fgangvisuals.module.settings.Setting;
import com.fgangvisuals.util.IMinecraft;
import com.fgangvisuals.util.QuickLogger;
import com.fgangvisuals.util.base.Instance;
import com.fgangvisuals.util.render.math.Animation;
import com.fgangvisuals.util.render.math.Easing;

import com.fgangvisuals.util.config.ConfigManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Module implements IMinecraft, QuickLogger {
    public static boolean suppressConfigSave;

    private final String name, desc;
    private final ModuleCategory category;
    private int key;
    private boolean enabled;
    private final Animation animation = new Animation(Easing.BACK_OUT, 450);

    public final MinecraftClient mc = MinecraftClient.getInstance();

    private final List<Setting> settings = new ArrayList<>();

    public Module() {
        ModuleInformation information = getClass().getAnnotation(ModuleInformation.class);

        this.name = information.moduleName();
        this.desc = information.moduleDesc();
        this.category = information.moduleCategory();
        this.key = information.moduleKeybind();
    }

    public List<Setting> getSettings() {
        return Arrays.stream(this.getClass().getDeclaredFields()).map(field -> {
            try {
                field.setAccessible(true);
                return field.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(field -> field instanceof Setting).map(field -> (Setting) field).collect(Collectors.toList());
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
            Interface.NotificationManager.post(this.name, enabled);
        }
        if (name.equals("ClickGui")) return;
        ClientSounds soundsModule = Instance.get(ClientSounds.class);
        if (soundsModule != null && (soundsModule.isEnabled() || this instanceof ClientSounds)) {
            ClientSounds.play(this.isEnabled());
        }
        if (!suppressConfigSave && !ConfigManager.isLoading) {
            ConfigManager.autoSave();
        }
    }

    protected ModeSetting modeCreate() {
        return new ModeSetting("Мод", "Vanilla", "Vanilla", "Grim", "Polar");
    }

    public void onEnable() {
        FGANGVisuals.getInstance().getEventBus().register(this);
    }

    public void onDisable() {
        FGANGVisuals.getInstance().getEventBus().unregister(this);
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }
}