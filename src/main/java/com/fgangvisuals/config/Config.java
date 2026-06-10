package com.fgangvisuals.config;

import net.neoforged.bus.api.SubscribeEvent;
import com.fgangvisuals.FGANGVisuals;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = FGANGVisuals.MOD_ID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // === GLOBAL ===
    public static final ModConfigSpec.IntValue GLOBAL_ACCENT_COLOR = BUILDER.comment("Global accent color").defineInRange("global.accentColor", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue GLOBAL_SHADOWS = BUILDER.define("global.shadows", true);

    // === ARMOR HUD ===
    public static final ModConfigSpec.BooleanValue ARMOR_HUD_ENABLED = BUILDER.define("armorHUD.enabled", true);
    public static final ModConfigSpec.IntValue ARMOR_HUD_X = BUILDER.defineInRange("armorHUD.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue ARMOR_HUD_Y = BUILDER.defineInRange("armorHUD.y", 4, 0, 3000);
    public static final ModConfigSpec.BooleanValue ARMOR_HUD_VERTICAL = BUILDER.define("armorHUD.vertical", false);
    public static final ModConfigSpec.BooleanValue ARMOR_HUD_DURABILITY = BUILDER.define("armorHUD.showDurability", true);
    public static final ModConfigSpec.BooleanValue ARMOR_HUD_PERCENTAGE = BUILDER.define("armorHUD.showPercentage", true);
    public static final ModConfigSpec.DoubleValue ARMOR_HUD_SCALE = BUILDER.defineInRange("armorHUD.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue ARMOR_HUD_COLOR = BUILDER.defineInRange("armorHUD.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue ARMOR_HUD_BACKGROUND = BUILDER.define("armorHUD.background", true);
    public static final ModConfigSpec.DoubleValue ARMOR_HUD_BG_ALPHA = BUILDER.defineInRange("armorHUD.bgAlpha", 0.5, 0.0, 1.0);

    // === POTION HUD ===
    public static final ModConfigSpec.BooleanValue POTION_HUD_ENABLED = BUILDER.define("potionHUD.enabled", true);
    public static final ModConfigSpec.IntValue POTION_HUD_X = BUILDER.defineInRange("potionHUD.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue POTION_HUD_Y = BUILDER.defineInRange("potionHUD.y", 120, 0, 3000);
    public static final ModConfigSpec.BooleanValue POTION_HUD_ICONS = BUILDER.define("potionHUD.showIcons", true);
    public static final ModConfigSpec.BooleanValue POTION_HUD_BACKGROUND = BUILDER.define("potionHUD.background", true);
    public static final ModConfigSpec.DoubleValue POTION_HUD_SCALE = BUILDER.defineInRange("potionHUD.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue POTION_HUD_COLOR = BUILDER.defineInRange("potionHUD.color", 0xFFFFFF, 0, 0xFFFFFF);

    // === KEYSTROKES ===
    public static final ModConfigSpec.BooleanValue KEYSTROKES_ENABLED = BUILDER.define("keystrokes.enabled", true);
    public static final ModConfigSpec.IntValue KEYSTROKES_X = BUILDER.defineInRange("keystrokes.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue KEYSTROKES_Y = BUILDER.defineInRange("keystrokes.y", 300, 0, 3000);
    public static final ModConfigSpec.DoubleValue KEYSTROKES_SCALE = BUILDER.defineInRange("keystrokes.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue KEYSTROKES_COLOR = BUILDER.defineInRange("keystrokes.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.IntValue KEYSTROKES_PRESSED_COLOR = BUILDER.defineInRange("keystrokes.pressedColor", 0xAAAAAA, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue KEYSTROKES_BACKGROUND = BUILDER.define("keystrokes.background", true);
    public static final ModConfigSpec.BooleanValue KEYSTROKES_MOUSE = BUILDER.define("keystrokes.showMouse", true);
    public static final ModConfigSpec.BooleanValue KEYSTROKES_SPACE = BUILDER.define("keystrokes.showSpace", true);

    // === CUSTOM CROSSHAIR ===
    public static final ModConfigSpec.BooleanValue CROSSHAIR_ENABLED = BUILDER.define("crosshair.enabled", true);
    public static final ModConfigSpec.BooleanValue CROSSHAIR_CUSTOM_IMAGE = BUILDER.define("crosshair.customImage", false);
    public static final ModConfigSpec.IntValue CROSSHAIR_COLOR = BUILDER.defineInRange("crosshair.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.IntValue CROSSHAIR_STYLE = BUILDER.defineInRange("crosshair.style", 0, 0, 10);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_SIZE = BUILDER.defineInRange("crosshair.size", 1.0, 0.1, 5.0);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_OPACITY = BUILDER.defineInRange("crosshair.opacity", 1.0, 0.1, 1.0);
    public static final ModConfigSpec.BooleanValue CROSSHAIR_DYNAMIC = BUILDER.define("crosshair.dynamic", true);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_GAP = BUILDER.defineInRange("crosshair.gap", 2.0, 0.0, 20.0);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_THICKNESS = BUILDER.defineInRange("crosshair.thickness", 1.0, 0.5, 5.0);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_LENGTH = BUILDER.defineInRange("crosshair.length", 4.0, 1.0, 20.0);
    public static final ModConfigSpec.BooleanValue CROSSHAIR_DOT = BUILDER.define("crosshair.dot", true);
    public static final ModConfigSpec.BooleanValue CROSSHAIR_OUTLINE = BUILDER.define("crosshair.outline", true);
    public static final ModConfigSpec.IntValue CROSSHAIR_OUTLINE_COLOR = BUILDER.defineInRange("crosshair.outlineColor", 0x000000, 0, 0xFFFFFF);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_ROTATION = BUILDER.defineInRange("crosshair.rotation", 0.0, 0.0, 360.0);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_OFFSET_X = BUILDER.defineInRange("crosshair.offsetX", 0.0, -50.0, 50.0);
    public static final ModConfigSpec.DoubleValue CROSSHAIR_OFFSET_Y = BUILDER.defineInRange("crosshair.offsetY", 0.0, -50.0, 50.0);

    // === HIT COLOR ===
    public static final ModConfigSpec.BooleanValue HIT_COLOR_ENABLED = BUILDER.define("hitColor.enabled", true);
    public static final ModConfigSpec.IntValue HIT_COLOR_VALUE = BUILDER.defineInRange("hitColor.color", 0xFF0000, 0, 0xFFFFFF);
    public static final ModConfigSpec.IntValue HIT_COLOR_DURATION = BUILDER.defineInRange("hitColor.duration", 200, 50, 2000);
    public static final ModConfigSpec.BooleanValue HIT_COLOR_ONLY_PLAYER = BUILDER.define("hitColor.onlyPlayerAttacks", true);

    // === TOGGLE SPRINT / SNEAK ===
    public static final ModConfigSpec.BooleanValue TOGGLE_SPRINT_ENABLED = BUILDER.define("toggleSprint.enabled", true);
    public static final ModConfigSpec.BooleanValue TOGGLE_SNEAK_ENABLED = BUILDER.define("toggleSneak.enabled", false);
    public static final ModConfigSpec.BooleanValue TOGGLE_SPRINT_DISPLAY = BUILDER.define("toggleSprint.display", true);
    public static final ModConfigSpec.IntValue TOGGLE_SPRINT_COLOR = BUILDER.defineInRange("toggleSprint.color", 0xFFFFFF, 0, 0xFFFFFF);

    // === CPS COUNTER ===
    public static final ModConfigSpec.BooleanValue CPS_ENABLED = BUILDER.define("cps.enabled", true);
    public static final ModConfigSpec.IntValue CPS_X = BUILDER.defineInRange("cps.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue CPS_Y = BUILDER.defineInRange("cps.y", 380, 0, 3000);
    public static final ModConfigSpec.DoubleValue CPS_SCALE = BUILDER.defineInRange("cps.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.BooleanValue CPS_COMBINED = BUILDER.define("cps.combined", false);
    public static final ModConfigSpec.IntValue CPS_COLOR = BUILDER.defineInRange("cps.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue CPS_BACKGROUND = BUILDER.define("cps.background", true);

    // === COORDINATES ===
    public static final ModConfigSpec.BooleanValue COORDS_ENABLED = BUILDER.define("coords.enabled", true);
    public static final ModConfigSpec.IntValue COORDS_X = BUILDER.defineInRange("coords.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue COORDS_Y = BUILDER.defineInRange("coords.y", 420, 0, 3000);
    public static final ModConfigSpec.BooleanValue COORDS_DIRECTION = BUILDER.define("coords.showDirection", true);
    public static final ModConfigSpec.BooleanValue COORDS_BIOME = BUILDER.define("coords.showBiome", false);
    public static final ModConfigSpec.DoubleValue COORDS_SCALE = BUILDER.defineInRange("coords.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue COORDS_COLOR = BUILDER.defineInRange("coords.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue COORDS_BACKGROUND = BUILDER.define("coords.background", true);

    // === FPS COUNTER ===
    public static final ModConfigSpec.BooleanValue FPS_ENABLED = BUILDER.define("fps.enabled", true);
    public static final ModConfigSpec.IntValue FPS_X = BUILDER.defineInRange("fps.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue FPS_Y = BUILDER.defineInRange("fps.y", 460, 0, 3000);
    public static final ModConfigSpec.DoubleValue FPS_SCALE = BUILDER.defineInRange("fps.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue FPS_COLOR = BUILDER.defineInRange("fps.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue FPS_BACKGROUND = BUILDER.define("fps.background", true);
    public static final ModConfigSpec.BooleanValue FPS_COLORED = BUILDER.define("fps.colored", true);

    // === PING DISPLAY ===
    public static final ModConfigSpec.BooleanValue PING_ENABLED = BUILDER.define("ping.enabled", true);
    public static final ModConfigSpec.IntValue PING_X = BUILDER.defineInRange("ping.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue PING_Y = BUILDER.defineInRange("ping.y", 480, 0, 3000);
    public static final ModConfigSpec.DoubleValue PING_SCALE = BUILDER.defineInRange("ping.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue PING_COLOR = BUILDER.defineInRange("ping.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue PING_COLORED = BUILDER.define("ping.colored", true);
    public static final ModConfigSpec.BooleanValue PING_BACKGROUND = BUILDER.define("ping.background", true);

    // === HIT PARTICLES ===
    public static final ModConfigSpec.BooleanValue HIT_PARTICLES_ENABLED = BUILDER.define("hitParticles.enabled", true);
    public static final ModConfigSpec.IntValue HIT_PARTICLE_COLOR = BUILDER.defineInRange("hitParticles.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.IntValue HIT_PARTICLE_COUNT = BUILDER.defineInRange("hitParticles.count", 8, 1, 50);
    public static final ModConfigSpec.DoubleValue HIT_PARTICLE_SIZE = BUILDER.defineInRange("hitParticles.size", 1.0, 0.1, 3.0);
    public static final ModConfigSpec.BooleanValue HIT_PARTICLES_ONLY_PLAYER = BUILDER.define("hitParticles.onlyPlayerAttacks", true);

    // === TRAIL EFFECT ===
    public static final ModConfigSpec.BooleanValue TRAIL_ENABLED = BUILDER.define("trail.enabled", true);
    public static final ModConfigSpec.IntValue TRAIL_COLOR = BUILDER.defineInRange("trail.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.DoubleValue TRAIL_LENGTH = BUILDER.defineInRange("trail.length", 1.0, 0.1, 5.0);
    public static final ModConfigSpec.DoubleValue TRAIL_WIDTH = BUILDER.defineInRange("trail.width", 0.05, 0.01, 0.5);
    public static final ModConfigSpec.IntValue TRAIL_MAX_POINTS = BUILDER.defineInRange("trail.maxPoints", 50, 10, 200);
    public static final ModConfigSpec.BooleanValue TRAIL_ONLY_SELF = BUILDER.define("trail.onlySelf", true);

    // === DYNAMIC LIGHTS ===
    public static final ModConfigSpec.BooleanValue DYNAMIC_LIGHTS_ENABLED = BUILDER.define("dynamicLights.enabled", false);
    public static final ModConfigSpec.DoubleValue DYNAMIC_LIGHT_RANGE = BUILDER.defineInRange("dynamicLights.range", 12.0, 5.0, 30.0);

    // === KILL EFFECTS ===
    public static final ModConfigSpec.BooleanValue KILL_EFFECTS_ENABLED = BUILDER.define("killEffects.enabled", true);
    public static final ModConfigSpec.IntValue KILL_EFFECT_STYLE = BUILDER.defineInRange("killEffects.style", 0, 0, 3);
    public static final ModConfigSpec.IntValue KILL_EFFECT_COLOR = BUILDER.defineInRange("killEffects.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue KILL_EFFECTS_ONLY_PLAYER = BUILDER.define("killEffects.onlyPlayerKills", true);
    public static final ModConfigSpec.BooleanValue KILL_EFFECT_SOUND = BUILDER.define("killEffects.sound", true);

    // === FULLBRIGHT ===
    public static final ModConfigSpec.BooleanValue FULLBRIGHT_ENABLED = BUILDER.define("fullbright.enabled", false);
    public static final ModConfigSpec.DoubleValue FULLBRIGHT_GAMMA = BUILDER.defineInRange("fullbright.gamma", 15.0, 1.0, 15.0);

    // === NO HURT CAM ===
    public static final ModConfigSpec.BooleanValue NO_HURT_CAM_ENABLED = BUILDER.define("noHurtCam.enabled", false);

    // === VIEW BOBBING ===
    public static final ModConfigSpec.BooleanValue REDUCED_BOBBING_ENABLED = BUILDER.define("viewBobbing.reduced", true);
    public static final ModConfigSpec.DoubleValue VIEW_BOBBING_AMOUNT = BUILDER.defineInRange("viewBobbing.amount", 0.3, 0.0, 1.0);

    // === OPTIMIZATIONS ===
    public static final ModConfigSpec.BooleanValue ENTITY_CULLING_ENABLED = BUILDER.define("optimizations.entityCulling", true);
    public static final ModConfigSpec.IntValue ENTITY_CULLING_DISTANCE = BUILDER.defineInRange("optimizations.entityCullingDistance", 64, 16, 256);
    public static final ModConfigSpec.BooleanValue SMART_CULLING = BUILDER.define("optimizations.smartCulling", true);
    public static final ModConfigSpec.BooleanValue PARTICLE_OPTIMIZER = BUILDER.define("optimizations.particleOptimizer", true);
    public static final ModConfigSpec.IntValue MAX_PARTICLES = BUILDER.defineInRange("optimizations.maxParticles", 500, 100, 5000);
    public static final ModConfigSpec.BooleanValue RENDER_OPTIMIZER = BUILDER.define("optimizations.renderOptimizer", false);
    public static final ModConfigSpec.BooleanValue FPS_SMOOTH = BUILDER.define("optimizations.fpsSmooth", false);
    public static final ModConfigSpec.BooleanValue BLOCK_ENTITY_OPTIMIZER = BUILDER.define("optimizations.blockEntityOptimizer", false);
    public static final ModConfigSpec.IntValue BLOCK_ENTITY_DISTANCE = BUILDER.defineInRange("optimizations.blockEntityDistance", 32, 8, 128);
    public static final ModConfigSpec.BooleanValue ENTITY_OPTIMIZER = BUILDER.define("optimizations.entityOptimizer", false);
    public static final ModConfigSpec.IntValue ENTITY_OPTIMIZER_DISTANCE = BUILDER.defineInRange("optimizations.entityOptimizerDistance", 48, 16, 128);
    public static final ModConfigSpec.BooleanValue MEMORY_OPTIMIZER = BUILDER.define("optimizations.memoryOptimizer", false);
    public static final ModConfigSpec.IntValue MEMORY_CLEANUP_INTERVAL = BUILDER.defineInRange("optimizations.memoryCleanupInterval", 300, 60, 1800);
    public static final ModConfigSpec.BooleanValue FAST_RENDER = BUILDER.define("optimizations.fastRender", false);
    public static final ModConfigSpec.BooleanValue LAZY_CHUNK_LOADING = BUILDER.define("optimizations.lazyChunkLoading", false);

    // === TARGET HUD ===
    public static final ModConfigSpec.BooleanValue TARGET_HUD_ENABLED = BUILDER.define("targetHUD.enabled", true);
    public static final ModConfigSpec.IntValue TARGET_HUD_X = BUILDER.defineInRange("targetHUD.x", 200, 0, 5000);
    public static final ModConfigSpec.IntValue TARGET_HUD_Y = BUILDER.defineInRange("targetHUD.y", 200, 0, 3000);
    public static final ModConfigSpec.DoubleValue TARGET_HUD_SCALE = BUILDER.defineInRange("targetHUD.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue TARGET_HUD_COLOR = BUILDER.defineInRange("targetHUD.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue TARGET_HUD_BACKGROUND = BUILDER.define("targetHUD.background", true);
    public static final ModConfigSpec.BooleanValue TARGET_HUD_SHOW_HEALTH = BUILDER.define("targetHUD.showHealth", true);
    public static final ModConfigSpec.BooleanValue TARGET_HUD_SHOW_ARMOR = BUILDER.define("targetHUD.showArmor", true);
    public static final ModConfigSpec.BooleanValue TARGET_HUD_SHOW_POTIONS = BUILDER.define("targetHUD.showPotions", true);

    // === REACH DISPLAY ===
    public static final ModConfigSpec.BooleanValue REACH_ENABLED = BUILDER.define("reach.enabled", true);
    public static final ModConfigSpec.IntValue REACH_X = BUILDER.defineInRange("reach.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue REACH_Y = BUILDER.defineInRange("reach.y", 500, 0, 3000);
    public static final ModConfigSpec.DoubleValue REACH_SCALE = BUILDER.defineInRange("reach.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue REACH_COLOR = BUILDER.defineInRange("reach.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue REACH_BACKGROUND = BUILDER.define("reach.background", true);
    public static final ModConfigSpec.IntValue REACH_DECIMALS = BUILDER.defineInRange("reach.decimals", 2, 0, 4);

    // === COMBO COUNTER ===
    public static final ModConfigSpec.BooleanValue COMBO_ENABLED = BUILDER.define("combo.enabled", true);
    public static final ModConfigSpec.IntValue COMBO_X = BUILDER.defineInRange("combo.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue COMBO_Y = BUILDER.defineInRange("combo.y", 520, 0, 3000);
    public static final ModConfigSpec.DoubleValue COMBO_SCALE = BUILDER.defineInRange("combo.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue COMBO_COLOR = BUILDER.defineInRange("combo.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue COMBO_BACKGROUND = BUILDER.define("combo.background", true);
    public static final ModConfigSpec.IntValue COMBO_RESET = BUILDER.defineInRange("combo.resetTime", 2000, 500, 10000);

    // === BLOCK OVERLAY ===
    public static final ModConfigSpec.BooleanValue BLOCK_OVERLAY_ENABLED = BUILDER.define("blockOverlay.enabled", true);
    public static final ModConfigSpec.IntValue BLOCK_OVERLAY_COLOR = BUILDER.defineInRange("blockOverlay.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.DoubleValue BLOCK_OVERLAY_ALPHA = BUILDER.defineInRange("blockOverlay.alpha", 0.3, 0.0, 1.0);
    public static final ModConfigSpec.BooleanValue BLOCK_OVERLAY_OUTLINE = BUILDER.define("blockOverlay.outline", true);
    public static final ModConfigSpec.BooleanValue BLOCK_OVERLAY_FILL = BUILDER.define("blockOverlay.fill", true);

    // === INVENTORY HUD ===
    public static final ModConfigSpec.BooleanValue INV_HUD_ENABLED = BUILDER.define("invHUD.enabled", true);
    public static final ModConfigSpec.IntValue INV_HUD_X = BUILDER.defineInRange("invHUD.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue INV_HUD_Y = BUILDER.defineInRange("invHUD.y", 260, 0, 3000);
    public static final ModConfigSpec.DoubleValue INV_HUD_SCALE = BUILDER.defineInRange("invHUD.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.BooleanValue INV_HUD_BACKGROUND = BUILDER.define("invHUD.background", true);
    public static final ModConfigSpec.BooleanValue INV_HUD_SHOW_ARMOR = BUILDER.define("invHUD.showArmor", true);

    // === TIME DISPLAY ===
    public static final ModConfigSpec.BooleanValue TIME_ENABLED = BUILDER.define("time.enabled", false);
    public static final ModConfigSpec.IntValue TIME_X = BUILDER.defineInRange("time.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue TIME_Y = BUILDER.defineInRange("time.y", 540, 0, 3000);
    public static final ModConfigSpec.DoubleValue TIME_SCALE = BUILDER.defineInRange("time.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue TIME_COLOR = BUILDER.defineInRange("time.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue TIME_BACKGROUND = BUILDER.define("time.background", true);
    public static final ModConfigSpec.BooleanValue TIME_24H = BUILDER.define("time.format24h", true);

    // === SESSION INFO ===
    public static final ModConfigSpec.BooleanValue SESSION_ENABLED = BUILDER.define("session.enabled", false);
    public static final ModConfigSpec.IntValue SESSION_X = BUILDER.defineInRange("session.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue SESSION_Y = BUILDER.defineInRange("session.y", 560, 0, 3000);
    public static final ModConfigSpec.DoubleValue SESSION_SCALE = BUILDER.defineInRange("session.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue SESSION_COLOR = BUILDER.defineInRange("session.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue SESSION_BACKGROUND = BUILDER.define("session.background", true);
    public static final ModConfigSpec.BooleanValue SESSION_SHOW_KILLS = BUILDER.define("session.showKills", true);
    public static final ModConfigSpec.BooleanValue SESSION_SHOW_DEATHS = BUILDER.define("session.showDeaths", true);
    public static final ModConfigSpec.BooleanValue SESSION_SHOW_KD = BUILDER.define("session.showKD", true);
    public static final ModConfigSpec.BooleanValue SESSION_SHOW_TIME = BUILDER.define("session.showTime", true);

    // === MEMORY DISPLAY ===
    public static final ModConfigSpec.BooleanValue MEMORY_DISPLAY_ENABLED = BUILDER.define("memoryDisplay.enabled", false);
    public static final ModConfigSpec.IntValue MEMORY_DISPLAY_X = BUILDER.defineInRange("memoryDisplay.x", 4, 0, 5000);
    public static final ModConfigSpec.IntValue MEMORY_DISPLAY_Y = BUILDER.defineInRange("memoryDisplay.y", 580, 0, 3000);
    public static final ModConfigSpec.DoubleValue MEMORY_DISPLAY_SCALE = BUILDER.defineInRange("memoryDisplay.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue MEMORY_DISPLAY_COLOR = BUILDER.defineInRange("memoryDisplay.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue MEMORY_DISPLAY_BACKGROUND = BUILDER.define("memoryDisplay.background", true);

    // === SCOREBOARD EDITOR ===
    public static final ModConfigSpec.BooleanValue SCOREBOARD_ENABLED = BUILDER.define("scoreboard.enabled", true);
    public static final ModConfigSpec.IntValue SCOREBOARD_X = BUILDER.defineInRange("scoreboard.x", 0, -500, 500);
    public static final ModConfigSpec.IntValue SCOREBOARD_Y = BUILDER.defineInRange("scoreboard.y", 0, -500, 500);
    public static final ModConfigSpec.DoubleValue SCOREBOARD_SCALE = BUILDER.defineInRange("scoreboard.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.BooleanValue SCOREBOARD_NO_NUMBERS = BUILDER.define("scoreboard.noNumbers", false);
    public static final ModConfigSpec.IntValue SCOREBOARD_TEXT_COLOR = BUILDER.defineInRange("scoreboard.textColor", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue SCOREBOARD_BACKGROUND = BUILDER.define("scoreboard.background", true);

    // === CHAT EDITOR ===
    public static final ModConfigSpec.BooleanValue CHAT_EDITOR_ENABLED = BUILDER.define("chat.enabled", true);
    public static final ModConfigSpec.DoubleValue CHAT_BG_ALPHA = BUILDER.defineInRange("chat.bgAlpha", 0.5, 0.0, 1.0);
    public static final ModConfigSpec.IntValue CHAT_WIDTH = BUILDER.defineInRange("chat.width", 320, 100, 600);
    public static final ModConfigSpec.IntValue CHAT_HEIGHT = BUILDER.defineInRange("chat.height", 180, 50, 400);
    public static final ModConfigSpec.IntValue CHAT_TEXT_COLOR = BUILDER.defineInRange("chat.textColor", 0xFFFFFF, 0, 0xFFFFFF);

    // === ZOOM ===
    public static final ModConfigSpec.BooleanValue ZOOM_ENABLED = BUILDER.define("zoom.enabled", true);
    public static final ModConfigSpec.DoubleValue ZOOM_FOV = BUILDER.defineInRange("zoom.fov", 30.0, 5.0, 90.0);
    public static final ModConfigSpec.BooleanValue ZOOM_SMOOTH = BUILDER.define("zoom.smooth", true);

    // === FREELOOK ===
    public static final ModConfigSpec.BooleanValue FREELOOK_ENABLED = BUILDER.define("freelook.enabled", true);
    public static final ModConfigSpec.BooleanValue FREELOOK_RETURN_ON_RELEASE = BUILDER.define("freelook.returnOnRelease", true);

    // === MOTION BLUR ===
    public static final ModConfigSpec.BooleanValue MOTION_BLUR_ENABLED = BUILDER.define("motionBlur.enabled", false);
    public static final ModConfigSpec.DoubleValue MOTION_BLUR_AMOUNT = BUILDER.defineInRange("motionBlur.amount", 0.5, 0.0, 1.0);

    // === NO OVERLAYS ===
    public static final ModConfigSpec.BooleanValue NO_OVERLAY_FIRE = BUILDER.define("noOverlay.fire", false);
    public static final ModConfigSpec.BooleanValue NO_OVERLAY_PUMPKIN = BUILDER.define("noOverlay.pumpkin", false);
    public static final ModConfigSpec.BooleanValue NO_OVERLAY_PORTAL = BUILDER.define("noOverlay.portal", false);
    public static final ModConfigSpec.BooleanValue NO_OVERLAY_BLINDNESS = BUILDER.define("noOverlay.blindness", false);
    public static final ModConfigSpec.BooleanValue NO_OVERLAY_NAUSEA = BUILDER.define("noOverlay.nausea", false);
    public static final ModConfigSpec.BooleanValue NO_OVERLAY_UNDERWATER = BUILDER.define("noOverlay.underwater", false);
    public static final ModConfigSpec.BooleanValue NO_OVERLAY_VIGNETTE = BUILDER.define("noOverlay.vignette", false);

    // === LOW FIRE ===
    public static final ModConfigSpec.BooleanValue LOW_FIRE_ENABLED = BUILDER.define("lowFire.enabled", false);
    public static final ModConfigSpec.DoubleValue LOW_FIRE_HEIGHT = BUILDER.defineInRange("lowFire.height", 0.3, 0.0, 1.0);

    // === CLEAR WATER / NO FOG / NO WEATHER ===
    public static final ModConfigSpec.BooleanValue CLEAR_WATER_ENABLED = BUILDER.define("clearWater.enabled", false);
    public static final ModConfigSpec.BooleanValue NO_FOG_ENABLED = BUILDER.define("noFog.enabled", false);
    public static final ModConfigSpec.BooleanValue NO_WEATHER_ENABLED = BUILDER.define("noWeather.enabled", false);

    // === SCREEN EFFECTS ===
    public static final ModConfigSpec.BooleanValue SCREEN_SHAKE_ENABLED = BUILDER.define("screenShake.enabled", false);
    public static final ModConfigSpec.DoubleValue SCREEN_SHAKE_AMOUNT = BUILDER.defineInRange("screenShake.amount", 0.5, 0.0, 2.0);
    public static final ModConfigSpec.BooleanValue SCREEN_FLASH_ENABLED = BUILDER.define("screenFlash.enabled", false);
    public static final ModConfigSpec.IntValue SCREEN_FLASH_COLOR = BUILDER.defineInRange("screenFlash.color", 0xFFFFFF, 0, 0xFFFFFF);

    // === HITBOX / CHUNK BORDERS ===
    public static final ModConfigSpec.BooleanValue HITBOX_ENABLED = BUILDER.define("hitbox.enabled", false);
    public static final ModConfigSpec.BooleanValue CHUNK_BORDERS_ENABLED = BUILDER.define("chunkBorders.enabled", false);

    // === MODULE LIST (ArrayList) ===
    public static final ModConfigSpec.BooleanValue ARRAYLIST_ENABLED = BUILDER.define("arraylist.enabled", false);
    public static final ModConfigSpec.IntValue ARRAYLIST_X = BUILDER.defineInRange("arraylist.x", 2, 0, 5000);
    public static final ModConfigSpec.IntValue ARRAYLIST_Y = BUILDER.defineInRange("arraylist.y", 2, 0, 3000);
    public static final ModConfigSpec.DoubleValue ARRAYLIST_SCALE = BUILDER.defineInRange("arraylist.scale", 1.0, 0.5, 2.0);
    public static final ModConfigSpec.IntValue ARRAYLIST_COLOR = BUILDER.defineInRange("arraylist.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.BooleanValue ARRAYLIST_BACKGROUND = BUILDER.define("arraylist.background", false);
    public static final ModConfigSpec.BooleanValue ARRAYLIST_RAINBOW = BUILDER.define("arraylist.rainbow", false);

    // === WATERMARK ===
    public static final ModConfigSpec.BooleanValue WATERMARK_ENABLED = BUILDER.define("watermark.enabled", false);
    public static final ModConfigSpec.IntValue WATERMARK_X = BUILDER.defineInRange("watermark.x", 2, 0, 5000);
    public static final ModConfigSpec.IntValue WATERMARK_Y = BUILDER.defineInRange("watermark.y", 2, 0, 3000);
    public static final ModConfigSpec.DoubleValue WATERMARK_SCALE = BUILDER.defineInRange("watermark.scale", 1.0, 0.5, 2.0);
    public static final ModConfigSpec.IntValue WATERMARK_COLOR = BUILDER.defineInRange("watermark.color", 0xFFFFFF, 0, 0xFFFFFF);
    public static final ModConfigSpec.ConfigValue<String> WATERMARK_TEXT = BUILDER.define("watermark.text", "FGANG Visuals");

    // === NOTIFICATIONS ===
    public static final ModConfigSpec.BooleanValue NOTIFICATIONS_ENABLED = BUILDER.define("notifications.enabled", true);
    public static final ModConfigSpec.IntValue NOTIFICATIONS_X = BUILDER.defineInRange("notifications.x", 2, 0, 5000);
    public static final ModConfigSpec.IntValue NOTIFICATIONS_Y = BUILDER.defineInRange("notifications.y", 200, 0, 3000);
    public static final ModConfigSpec.DoubleValue NOTIFICATIONS_SCALE = BUILDER.defineInRange("notifications.scale", 1.0, 0.5, 2.0);
    public static final ModConfigSpec.IntValue NOTIFICATIONS_DURATION = BUILDER.defineInRange("notifications.duration", 2000, 500, 10000);

    // === TNT TIMER ===
    public static final ModConfigSpec.BooleanValue TNT_TIMER_ENABLED = BUILDER.define("tntTimer.enabled", true);
    public static final ModConfigSpec.IntValue TNT_TIMER_COLOR = BUILDER.defineInRange("tntTimer.color", 0xFF0000, 0, 0xFFFFFF);

    // === POT COUNTER ===
    public static final ModConfigSpec.BooleanValue POT_COUNTER_ENABLED = BUILDER.define("potCounter.enabled", false);
    public static final ModConfigSpec.IntValue POT_COUNTER_X = BUILDER.defineInRange("potCounter.x", 100, 0, 5000);
    public static final ModConfigSpec.IntValue POT_COUNTER_Y = BUILDER.defineInRange("potCounter.y", 100, 0, 3000);
    public static final ModConfigSpec.DoubleValue POT_COUNTER_SCALE = BUILDER.defineInRange("potCounter.scale", 1.0, 0.5, 3.0);
    public static final ModConfigSpec.IntValue POT_COUNTER_COLOR = BUILDER.defineInRange("potCounter.color", 0xFFFFFF, 0, 0xFFFFFF);

    // === W-TAP INDICATOR ===
    public static final ModConfigSpec.BooleanValue WTAP_ENABLED = BUILDER.define("wTap.enabled", false);
    public static final ModConfigSpec.IntValue WTAP_X = BUILDER.defineInRange("wTap.x", 100, 0, 5000);
    public static final ModConfigSpec.IntValue WTAP_Y = BUILDER.defineInRange("wTap.y", 80, 0, 3000);
    public static final ModConfigSpec.DoubleValue WTAP_SCALE = BUILDER.defineInRange("wTap.scale", 1.0, 0.5, 3.0);

    // === HEALTH BAR ESP ===
    public static final ModConfigSpec.BooleanValue HEALTH_BAR_ESP_ENABLED = BUILDER.define("healthBarESP.enabled", false);
    public static final ModConfigSpec.DoubleValue HEALTH_BAR_ESP_HEIGHT = BUILDER.defineInRange("healthBarESP.height", 0.3, 0.1, 2.0);
    public static final ModConfigSpec.DoubleValue HEALTH_BAR_ESP_WIDTH = BUILDER.defineInRange("healthBarESP.width", 1.0, 0.5, 2.0);

    // === GUI SETTINGS ===
    public static final ModConfigSpec.BooleanValue GUI_ANIMATIONS = BUILDER.define("gui.animations", true);
    public static final ModConfigSpec.DoubleValue GUI_ANIMATION_SPEED = BUILDER.defineInRange("gui.animationSpeed", 1.0, 0.1, 3.0);
    public static final ModConfigSpec.BooleanValue GUI_BLUR = BUILDER.define("gui.blur", false);
    public static final ModConfigSpec.IntValue GUI_ACCENT_COLOR = BUILDER.defineInRange("gui.accentColor", 0xFFFFFF, 0, 0xFFFFFF);

    public static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}
