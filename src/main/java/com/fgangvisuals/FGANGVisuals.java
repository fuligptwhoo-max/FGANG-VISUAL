package com.fgangvisuals;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.fgangvisuals.event.list.EventKeyInput;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleStorage;
import com.fgangvisuals.module.list.render.ClickGui;
import com.fgangvisuals.module.list.render.HitSounds;
import com.fgangvisuals.module.settings.Setting;
import com.fgangvisuals.util.commands.CommandDispatcher;
import com.fgangvisuals.util.commands.manager.CommandRepository;
import com.fgangvisuals.util.config.ConfigManager;
import com.fgangvisuals.util.draggable.DragManager;
import com.fgangvisuals.util.friend.FriendRepository;
import com.fgangvisuals.util.macro.MacroRepository;
import com.fgangvisuals.util.math.TPSGetter;
import com.fgangvisuals.util.player.combat.IdealHitUtils;
import com.fgangvisuals.util.player.other.ServerManager;
import com.fgangvisuals.util.rotation.ComponentManager;
import com.fgangvisuals.util.script.ScriptManager;
import com.fgangvisuals.util.staff.StaffManager;

import java.io.File;

public class FGANGVisuals implements ModInitializer {

    private static FGANGVisuals instance;

    @Getter
    private final EventBus eventBus;

    @Getter
    private final ModuleStorage moduleStorage;
    @Getter
    private final ComponentManager componentManager;
    @Getter
    private final DragManager dragManager;
    @Getter
    private final CommandRepository commandRepository;
    @Getter
    private final MacroRepository macroRepository;
    @Getter
    private final ConfigManager configManager;
    @Getter
    private final CommandDispatcher commandDispatcher;
    @Getter
    private final StaffManager staffManager;
    @Getter
    private final ServerManager serverManager;
    @Getter
    private final TPSGetter tpsGetter;
    @Getter
    private final IdealHitUtils idealHitUtils;
    @Getter
    private final ScriptManager scriptManager;


    public FGANGVisuals() {
        instance = this;

        eventBus = new EventBus();
        eventBus.register(this);



        moduleStorage = new ModuleStorage();
        componentManager = new ComponentManager();
        dragManager = new DragManager();
        macroRepository = new MacroRepository();
        configManager = new ConfigManager();
        staffManager = new StaffManager();
        staffManager.load();
        commandRepository = new CommandRepository();
        commandDispatcher = new CommandDispatcher();
        serverManager = new ServerManager();
        tpsGetter = new TPSGetter();
        idealHitUtils = new IdealHitUtils();
        scriptManager = new ScriptManager();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConfigManager.save("autocfg");
            getDragManager().saveDraggables();
            getMacroRepository().save();
            FriendRepository.save();
            staffManager.save();
        }));
        File dir = new File("fgangvisuals/configs/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static FGANGVisuals getInstance() {
        return instance == null ? new FGANGVisuals() : instance;
    }

    @Override
    public void onInitialize() {
        getModuleStorage().injectRegisterModules();
        HitSounds.registerSounds();
        componentManager.init();
        dragManager.load();
        macroRepository.load();
        FriendRepository.load();
        configManager.load("autocfg");

        for (Module module : moduleStorage.getModules()) {
            for (Setting setting : module.getSettings()) {
                setting.setOnChange(() -> ConfigManager.autoSave());
            }
        }

        registerClickGuiKeybind();
    }

    private void registerClickGuiKeybind() {
        KeyBinding clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fgangvisuals.clickgui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.fgangvisuals"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClickGui clickGui = getModuleStorage().get(ClickGui.class);
            if (clickGui != null) {
                clickGui.setKey(clickGuiKey.getDefaultKey().getCode());
            }
        });
    }

    @Subscribe
    private void onModuleKeyPressed(EventKeyInput event) {
        if (event.getAction() != 1 || MinecraftClient.getInstance().currentScreen != null) return;
        Module.suppressConfigSave = true;
        for (Module module : getModuleStorage().getModules()) {
            if (module.getKey() == event.getKey()) {
                module.toggle();
            }
        }
        Module.suppressConfigSave = false;
    }
}