package com.fgangvisuals;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import com.fgangvisuals.event.list.EventKeyInput;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleStorage;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FGANGVisuals implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("fgangvisuals");

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
        LOGGER.info("FGANGVisuals main entrypoint initialized");

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

        LOGGER.info("FGANGVisuals initialized with {} modules", moduleStorage.getModules().size());
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