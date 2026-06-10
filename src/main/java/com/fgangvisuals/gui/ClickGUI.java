package com.fgangvisuals.gui;

import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.config.Config;
import com.fgangvisuals.gui.components.CategoryPanel;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    private final Minecraft mc = Minecraft.getInstance();
    private final List<CategoryPanel> panels = new ArrayList<>();
    private int selectedPanel = 0;
    private float animProgress = 0f;

    private static final int BG = 0xDD111111;
    private static final int PANEL_BG = 0xFF1A1A1A;
    private static final int BORDER = 0xFF333333;
    private static final int ACCENT = 0xFFFFFFFF;
    private static final int HOVER = 0xFF222222;

    private static final String[] CATEGORIES = {"PVP", "Visuals", "Optimize", "Crosshair", "GUI"};

    public ClickGUI() {
        super(Component.literal("FGANG Visuals"));
    }

    @Override
    protected void init() {
        super.init();
        panels.clear();
        int panelW = 100;
        int panelH = 20;
        int startX = 10;
        int startY = 30;
        int gap = 4;
        for (int i = 0; i < CATEGORIES.length; i++) {
            panels.add(new CategoryPanel(CATEGORIES[i], startX, startY + i * (panelH + gap), panelW, panelH, i));
        }
        animProgress = 0f;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        animProgress = RenderUtils.lerp(animProgress, 1f, 0.2f);
        float a = MathUtils.easeOutExpo(animProgress);

        // Background
        g.fill(0, 0, this.width, this.height, BG);

        // Title
        String title = "§lFGANG Visuals §7v1.0";
        g.drawString(mc.font, title, 10, 10, ACCENT, true);
        g.fill(10, 24, 210, 25, BORDER);

        // Panels
        for (int i = 0; i < panels.size(); i++) {
            CategoryPanel p = panels.get(i);
            p.render(g, mx, my, i == selectedPanel, a);
        }

        // Content area
        int contentX = 120;
        int contentY = 30;
        int contentW = Math.max(200, this.width - contentX - 10);
        int contentH = this.height - 40;
        g.fill(contentX, contentY, contentX + contentW, contentY + contentH, PANEL_BG);
        drawBorder(g, contentX, contentY, contentW, contentH, BORDER);

        // Category title
        g.drawString(mc.font, "§l" + CATEGORIES[selectedPanel], contentX + 8, contentY + 8, ACCENT, true);
        g.fill(contentX + 8, contentY + 22, contentX + contentW - 8, contentY + 23, BORDER);

        renderFeatures(g, selectedPanel, contentX + 8, contentY + 28, contentW - 16, mx, my);

        // Footer
        g.drawString(mc.font, "§7ESC to close", 10, this.height - 14, 0x888888, false);
    }

    private void renderFeatures(GuiGraphics g, int cat, int x, int y, int w, int mx, int my) {
        String[][] features = {
            // PVP
            {"Armor HUD", "Potion HUD", "KeyStrokes", "Crosshair", "Hit Color", "Toggle Sprint",
                "CPS Counter", "Coordinates", "FPS Counter", "Ping Display", "Target HUD", "Reach Display",
                "Combo Counter", "Inventory HUD", "Time Display", "Session Info", "Memory Display",
                "TNT Timer", "Pot Counter", "W-Tap Indicator"},
            // Visuals
            {"Hit Particles", "Trail Effect", "Dynamic Lights", "Kill Effects", "FullBright",
                "No Hurt Cam", "Reduced Bobbing", "Block Overlay", "Zoom", "Freelook", "Motion Blur",
                "No Overlay", "Low Fire", "Clear Water", "No Fog", "No Weather", "Screen Shake",
                "Screen Flash", "Hitbox", "Chunk Borders", "Health Bar ESP", "Scoreboard", "Chat"},
            // Optimize
            {"Entity Culling", "Particle Optimizer", "Render Optimizer", "FPS Smoothing",
                "BlockEntity Optimize", "Entity Optimize", "Memory Cleanup", "Fast Render"},
            // Crosshair
            {"Crosshair Style", "Crosshair Size", "Color", "Opacity", "Gap", "Length",
                "Thickness", "Rotation", "Offset X/Y", "Load Image", "Dot", "Outline"},
            // GUI
            {"Module List", "Watermark", "Notifications", "ClickGUI Key", "HUD Editor Key",
                "Animations", "Animation Speed", "Accent Color"}
        };
        boolean[][] toggles = {
            {Config.ARMOR_HUD_ENABLED.get(), Config.POTION_HUD_ENABLED.get(), Config.KEYSTROKES_ENABLED.get(),
                Config.CROSSHAIR_ENABLED.get(), Config.HIT_COLOR_ENABLED.get(), Config.TOGGLE_SPRINT_ENABLED.get(),
                Config.CPS_ENABLED.get(), Config.COORDS_ENABLED.get(), Config.FPS_ENABLED.get(), Config.PING_ENABLED.get(),
                Config.TARGET_HUD_ENABLED.get(), Config.REACH_ENABLED.get(), Config.COMBO_ENABLED.get(),
                Config.INV_HUD_ENABLED.get(), Config.TIME_ENABLED.get(), Config.SESSION_ENABLED.get(),
                Config.MEMORY_DISPLAY_ENABLED.get(), Config.TNT_TIMER_ENABLED.get(), Config.POT_COUNTER_ENABLED.get(),
                Config.WTAP_ENABLED.get()},
            {Config.HIT_PARTICLES_ENABLED.get(), Config.TRAIL_ENABLED.get(), Config.DYNAMIC_LIGHTS_ENABLED.get(),
                Config.KILL_EFFECTS_ENABLED.get(), Config.FULLBRIGHT_ENABLED.get(), Config.NO_HURT_CAM_ENABLED.get(),
                Config.REDUCED_BOBBING_ENABLED.get(), Config.BLOCK_OVERLAY_ENABLED.get(), Config.ZOOM_ENABLED.get(),
                Config.FREELOOK_ENABLED.get(), Config.MOTION_BLUR_ENABLED.get(), Config.NO_OVERLAY_FIRE.get(),
                Config.LOW_FIRE_ENABLED.get(), Config.CLEAR_WATER_ENABLED.get(), Config.NO_FOG_ENABLED.get(),
                Config.NO_WEATHER_ENABLED.get(), Config.SCREEN_SHAKE_ENABLED.get(), Config.SCREEN_FLASH_ENABLED.get(),
                Config.HITBOX_ENABLED.get(), Config.CHUNK_BORDERS_ENABLED.get(), Config.HEALTH_BAR_ESP_ENABLED.get(),
                Config.SCOREBOARD_ENABLED.get(), Config.CHAT_EDITOR_ENABLED.get()},
            {Config.ENTITY_CULLING_ENABLED.get(), Config.PARTICLE_OPTIMIZER.get(), Config.RENDER_OPTIMIZER.get(),
                Config.FPS_SMOOTH.get(), Config.BLOCK_ENTITY_OPTIMIZER.get(), Config.ENTITY_OPTIMIZER.get(),
                Config.MEMORY_OPTIMIZER.get(), Config.FAST_RENDER.get()},
            {false, false, false, false, false, false, false, false, false, false, Config.CROSSHAIR_DOT.get(), Config.CROSSHAIR_OUTLINE.get()},
            {Config.ARRAYLIST_ENABLED.get(), Config.WATERMARK_ENABLED.get(), Config.NOTIFICATIONS_ENABLED.get(),
                false, false, Config.GUI_ANIMATIONS.get(), false, false}
        };

        if (cat >= features.length) return;
        int rowH = 18;
        for (int i = 0; i < features[cat].length; i++) {
            int ry = y + 4 + i * rowH;
            if (ry > y + contentH - 30) break;
            boolean hover = mx >= x && mx <= x + w && my >= ry && my <= ry + rowH;
            if (hover) g.fill(x, ry, x + w, ry + rowH, HOVER);

            String name = features[cat][i];
            g.drawString(mc.font, name, x + 4, ry + 4, hover ? ACCENT : 0xCCCCCC, false);

            if (cat < toggles.length && i < toggles[cat].length && toggles[cat][i]) {
                int tw = 8;
                int tx = x + w - 16;
                int ty = ry + 5;
                g.fill(tx, ty, tx + tw, ty + tw, 0xFF33FF33);
                drawBorder(g, tx, ty, tw, tw, 0xFF228822);
            }
        }
    }

    private void drawBorder(GuiGraphics g, int x, int y, int w, int h, int c) {
        g.fill(x, y, x + w, y + 1, c);
        g.fill(x, y + h - 1, x + w, y + h, c);
        g.fill(x, y, x + 1, y + h, c);
        g.fill(x + w - 1, y, x + w, y + h, c);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).isMouseOver((int) mx, (int) my)) {
                selectedPanel = i;
                return true;
            }
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            mc.setScreen(null);
            return true;
        }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public boolean isPauseScreen() { return false; }

    private static class MathUtils {
        public static float lerp(float s, float e, float f) { return s + (e - s) * f; }
        public static float easeOutExpo(float x) { return x == 1 ? 1 : (float)(1 - Math.pow(2, -10 * x)); }
    }
}
