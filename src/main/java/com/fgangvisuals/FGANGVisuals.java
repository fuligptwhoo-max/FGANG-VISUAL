package com.fgangvisuals;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.config.ConfigScreen;
import com.fgangvisuals.features.optimizations.*;
import com.fgangvisuals.features.pvp.*;
import com.fgangvisuals.features.visuals.*;
import com.fgangvisuals.gui.ClickGUI;
import com.fgangvisuals.gui.HUDEditorScreen;
import com.fgangvisuals.gui.ModuleList;
import com.fgangvisuals.gui.Notifications;
import com.fgangvisuals.gui.Watermark;
import com.fgangvisuals.crosshair.CrosshairManager;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ClientTickEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(FGANGVisuals.MOD_ID)
public class FGANGVisuals {
    public static final String MOD_ID = "fgangvisuals";
    public static final String MOD_NAME = "FGANG Visuals";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Minecraft mc = Minecraft.getInstance();

    private static FGANGVisuals INSTANCE;

    public static KeyMapping clickGuiKey;
    public static KeyMapping hudEditorKey;

    // PVP
    public ArmorHUD armorHUD;
    public PotionHUD potionHUD;
    public KeyStrokes keyStrokes;
    public CustomCrosshair customCrosshair;
    public HitColor hitColor;
    public ToggleSprint toggleSprint;
    public CPSCounter cpsCounter;
    public Coordinates coordinates;
    public FPSCounter fpsCounter;
    public PingDisplay pingDisplay;
    public TargetHUD targetHUD;
    public ReachDisplay reachDisplay;
    public ComboCounter comboCounter;
    public InventoryHUD inventoryHUD;
    public TimeDisplay timeDisplay;
    public SessionInfo sessionInfo;
    public MemoryDisplay memoryDisplay;
    public TNTTimer tntTimer;
    public PotCounter potCounter;
    public WTapIndicator wTapIndicator;

    // Visuals
    public HitParticles hitParticles;
    public TrailEffect trailEffect;
    public DynamicLights dynamicLights;
    public KillEffects killEffects;
    public FullBright fullBright;
    public NoHurtCam noHurtCam;
    public ViewBobbing viewBobbing;
    public BlockOverlay blockOverlay;
    public Zoom zoom;
    public Freelook freelook;
    public MotionBlur motionBlur;
    public NoOverlay noOverlay;
    public LowFire lowFire;
    public EnvironmentEditor environmentEditor;
    public ScreenEffects screenEffects;
    public HitboxChunkBorders hitboxChunkBorders;
    public HealthBarESP healthBarESP;
    public ScoreboardEditor scoreboardEditor;
    public ChatEditor chatEditor;

    // Optimizations
    public EntityCulling entityCulling;
    public ParticleOptimizer particleOptimizer;
    public RenderOptimizer renderOptimizer;
    public FPSSmooth fpsSmooth;
    public BlockEntityOptimizer blockEntityOptimizer;
    public EntityOptimizer entityOptimizer;
    public MemoryOptimizer memoryOptimizer;

    // GUI
    public ClickGUI clickGUI;
    public ModuleList moduleList;
    public Watermark watermark;
    public Notifications notifications;
    public CrosshairManager crosshairManager;

    public FGANGVisuals(IEventBus modEventBus, ModContainer modContainer) {
        INSTANCE = this;
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, IConfigScreenFactory.
            (minecraft, parent) -> new ConfigScreen(parent));
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerKeybinds);
        NeoForge.EVENT_BUS.register(this);
        LOGGER.info("[FGANG Visuals] Mod initialized!");
    }

    public static FGANGVisuals getInstance() {
        return INSTANCE;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[FGANG Visuals] Common setup complete");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            crosshairManager = new CrosshairManager();
            initFeatures();
            LOGGER.info("[FGANG Visuals] All features initialized!");
        });
    }

    private void registerKeybinds(RegisterKeyMappingsEvent event) {
        clickGuiKey = new KeyMapping("key.fgangvisuals.clickgui", GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.fgangvisuals");
        hudEditorKey = new KeyMapping("key.fgangvisuals.hudeditor", GLFW.GLFW_KEY_UNKNOWN, "key.categories.fgangvisuals");
        event.register(clickGuiKey);
        event.register(hudEditorKey);
        if (zoom != null) zoom.registerKey(event);
        if (freelook != null) freelook.registerKey(event);
    }

    private void initFeatures() {
        // PVP
        armorHUD = new ArmorHUD();
        potionHUD = new PotionHUD();
        keyStrokes = new KeyStrokes();
        customCrosshair = new CustomCrosshair();
        hitColor = new HitColor();
        toggleSprint = new ToggleSprint();
        cpsCounter = new CPSCounter();
        coordinates = new Coordinates();
        fpsCounter = new FPSCounter();
        pingDisplay = new PingDisplay();
        targetHUD = new TargetHUD();
        reachDisplay = new ReachDisplay();
        comboCounter = new ComboCounter();
        inventoryHUD = new InventoryHUD();
        timeDisplay = new TimeDisplay();
        sessionInfo = new SessionInfo();
        memoryDisplay = new MemoryDisplay();
        tntTimer = new TNTTimer();
        potCounter = new PotCounter();
        wTapIndicator = new WTapIndicator();

        // Visuals
        hitParticles = new HitParticles();
        trailEffect = new TrailEffect();
        dynamicLights = new DynamicLights();
        killEffects = new KillEffects();
        fullBright = new FullBright();
        noHurtCam = new NoHurtCam();
        viewBobbing = new ViewBobbing();
        blockOverlay = new BlockOverlay();
        zoom = new Zoom();
        freelook = new Freelook();
        motionBlur = new MotionBlur();
        noOverlay = new NoOverlay();
        lowFire = new LowFire();
        environmentEditor = new EnvironmentEditor();
        screenEffects = new ScreenEffects();
        hitboxChunkBorders = new HitboxChunkBorders();
        healthBarESP = new HealthBarESP();
        scoreboardEditor = new ScoreboardEditor();
        chatEditor = new ChatEditor();

        // Optimizations
        entityCulling = new EntityCulling();
        particleOptimizer = new ParticleOptimizer();
        renderOptimizer = new RenderOptimizer();
        fpsSmooth = new FPSSmooth();
        blockEntityOptimizer = new BlockEntityOptimizer();
        entityOptimizer = new EntityOptimizer();
        memoryOptimizer = new MemoryOptimizer();

        // GUI
        clickGUI = new ClickGUI();
        moduleList = new ModuleList();
        watermark = new Watermark();
        notifications = new Notifications();

        registerFeatures();
    }

    private void registerFeatures() {
        // PVP
        NeoForge.EVENT_BUS.register(armorHUD);
        NeoForge.EVENT_BUS.register(potionHUD);
        NeoForge.EVENT_BUS.register(keyStrokes);
        NeoForge.EVENT_BUS.register(customCrosshair);
        NeoForge.EVENT_BUS.register(hitColor);
        NeoForge.EVENT_BUS.register(toggleSprint);
        NeoForge.EVENT_BUS.register(cpsCounter);
        NeoForge.EVENT_BUS.register(coordinates);
        NeoForge.EVENT_BUS.register(fpsCounter);
        NeoForge.EVENT_BUS.register(pingDisplay);
        NeoForge.EVENT_BUS.register(targetHUD);
        NeoForge.EVENT_BUS.register(reachDisplay);
        NeoForge.EVENT_BUS.register(comboCounter);
        NeoForge.EVENT_BUS.register(inventoryHUD);
        NeoForge.EVENT_BUS.register(timeDisplay);
        NeoForge.EVENT_BUS.register(sessionInfo);
        NeoForge.EVENT_BUS.register(memoryDisplay);
        NeoForge.EVENT_BUS.register(tntTimer);
        NeoForge.EVENT_BUS.register(potCounter);
        NeoForge.EVENT_BUS.register(wTapIndicator);

        // Visuals
        NeoForge.EVENT_BUS.register(hitParticles);
        NeoForge.EVENT_BUS.register(trailEffect);
        NeoForge.EVENT_BUS.register(dynamicLights);
        NeoForge.EVENT_BUS.register(killEffects);
        NeoForge.EVENT_BUS.register(fullBright);
        NeoForge.EVENT_BUS.register(noHurtCam);
        NeoForge.EVENT_BUS.register(viewBobbing);
        NeoForge.EVENT_BUS.register(blockOverlay);
        NeoForge.EVENT_BUS.register(zoom);
        NeoForge.EVENT_BUS.register(freelook);
        NeoForge.EVENT_BUS.register(motionBlur);
        NeoForge.EVENT_BUS.register(noOverlay);
        NeoForge.EVENT_BUS.register(lowFire);
        NeoForge.EVENT_BUS.register(environmentEditor);
        NeoForge.EVENT_BUS.register(screenEffects);
        NeoForge.EVENT_BUS.register(hitboxChunkBorders);
        NeoForge.EVENT_BUS.register(healthBarESP);
        NeoForge.EVENT_BUS.register(scoreboardEditor);
        NeoForge.EVENT_BUS.register(chatEditor);

        // Optimizations
        NeoForge.EVENT_BUS.register(entityCulling);
        NeoForge.EVENT_BUS.register(particleOptimizer);
        NeoForge.EVENT_BUS.register(renderOptimizer);
        NeoForge.EVENT_BUS.register(fpsSmooth);
        NeoForge.EVENT_BUS.register(blockEntityOptimizer);
        NeoForge.EVENT_BUS.register(entityOptimizer);
        NeoForge.EVENT_BUS.register(memoryOptimizer);

        // GUI
        NeoForge.EVENT_BUS.register(clickGUI);
        NeoForge.EVENT_BUS.register(moduleList);
        NeoForge.EVENT_BUS.register(watermark);
        NeoForge.EVENT_BUS.register(notifications);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if (mc.player == null) return;
        if (clickGuiKey != null && clickGuiKey.consumeClick()) {
            mc.setScreen(clickGUI);
        }
        if (hudEditorKey != null && hudEditorKey.consumeClick()) {
            mc.setScreen(new HUDEditorScreen());
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            FGANGVisuals.getInstance().clientSetup(event);
        }
    }
}
