package com.fgangvisuals.gui;

import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class HUDEditorScreen extends Screen {
    public HUDEditorScreen() {
        super(Component.literal("HUD Editor"));
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        g.fill(0, 0, this.width, this.height, 0xCC111111);
        g.drawCenteredString(this.font, "§lHUD Editor", this.width / 2, 10, 0xFFFFFFFF);
        g.drawCenteredString(this.font, "§7Drag elements to reposition (coming soon)", this.width / 2, 30, 0x888888);

        // Draw approximate HUD positions
        drawPlaceholder(g, "ArmorHUD", Config.ARMOR_HUD_X.get(), Config.ARMOR_HUD_Y.get(), 80, 40);
        drawPlaceholder(g, "PotionHUD", Config.POTION_HUD_X.get(), Config.POTION_HUD_Y.get(), 100, 60);
        drawPlaceholder(g, "KeyStrokes", Config.KEYSTROKES_X.get(), Config.KEYSTROKES_Y.get(), 70, 80);
        drawPlaceholder(g, "CPS", Config.CPS_X.get(), Config.CPS_Y.get(), 60, 24);
        drawPlaceholder(g, "Coords", Config.COORDS_X.get(), Config.COORDS_Y.get(), 90, 30);
        drawPlaceholder(g, "FPS", Config.FPS_X.get(), Config.FPS_Y.get(), 50, 20);
        drawPlaceholder(g, "Ping", Config.PING_X.get(), Config.PING_Y.get(), 60, 20);
    }

    private void drawPlaceholder(GuiGraphics g, String name, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h, 0x44222222);
        g.fill(x, y, x + w, y + 1, 0xFFFFFFFF);
        g.fill(x, y + h - 1, x + w, y + h, 0xFFFFFFFF);
        g.fill(x, y, x + 1, y + h, 0xFFFFFFFF);
        g.fill(x + w - 1, y, x + w, y + h, 0xFFFFFFFF);
        g.drawString(this.minecraft.font, name, x + 2, y + 2, 0xFFFFFFFF, true);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.setScreen(null);
            return true;
        }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
