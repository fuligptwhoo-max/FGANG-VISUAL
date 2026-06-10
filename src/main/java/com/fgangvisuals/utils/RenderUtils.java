package com.fgangvisuals.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class RenderUtils {
    private static final Minecraft mc = Minecraft.getInstance();

    public static final int WHITE = 0xFFFFFF;
    public static final int GRAY = 0xAAAAAA;
    public static final int DARK_GRAY = 0x555555;
    public static final int BLACK = 0x000000;
    public static final int RED = 0xFF3333;
    public static final int GREEN = 0x33FF33;
    public static final int YELLOW = 0xFFFF33;

    public static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    public static int withAlphaF(int color, float alpha) {
        return withAlpha(color, (int) (alpha * 255));
    }

    public static void drawRect(GuiGraphics g, int x, int y, int w, int h, int color) {
        g.fill(x, y, x + w, y + h, color);
    }

    public static void drawBorder(GuiGraphics g, int x, int y, int w, int h, int color) {
        g.fill(x, y, x + w, y + 1, color);
        g.fill(x, y + h - 1, x + w, y + h, color);
        g.fill(x, y, x + 1, y + h, color);
        g.fill(x + w - 1, y, x + w, y + h, color);
    }

    public static void drawBorderedRect(GuiGraphics g, int x, int y, int w, int h, int fill, int border) {
        drawRect(g, x, y, w, h, fill);
        drawBorder(g, x, y, w, h, border);
    }

    public static void drawMinimalBackground(GuiGraphics g, int x, int y, int w, int h, float alpha) {
        int bg = withAlphaF(0x111111, alpha);
        int border = withAlphaF(0x333333, alpha + 0.2f);
        drawBorderedRect(g, x, y, w, h, bg, border);
    }

    public static void drawStringWithShadow(GuiGraphics g, Font font, String text, int x, int y, int color) {
        g.drawString(font, text, x, y, color, true);
    }

    public static void drawCenteredString(GuiGraphics g, Font font, String text, int x, int y, int w, int color) {
        int tw = font.width(text);
        g.drawString(font, text, x + (w - tw) / 2, y, color, true);
    }

    public static int getStringWidth(String text) {
        return mc.font.width(text);
    }

    public static void drawProgressBar(GuiGraphics g, int x, int y, int w, int h, float progress, int bg, int fill) {
        drawRect(g, x, y, w, h, bg);
        int fw = (int) (w * Math.min(progress, 1.0f));
        if (fw > 0) drawRect(g, x, y, fw, h, fill);
        drawBorder(g, x, y, w, h, 0xFF333333);
    }

    public static void drawItemWithDurability(GuiGraphics g, int x, int y, net.minecraft.world.item.ItemStack stack, float scale) {
        if (stack.isEmpty()) return;
        int size = (int) (16 * scale);
        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        g.renderItem(stack, (int) (x / scale), (int) (y / scale));
        g.pose().popPose();

        if (stack.isDamageableItem()) {
            int max = stack.getMaxDamage();
            int cur = max - stack.getDamageValue();
            float pct = (float) cur / max;
            int bw = (int) (13 * scale);
            int bh = Math.max(1, (int) (2 * scale));
            int bx = x + 2;
            int by = y + size - bh - 1;
            int c = pct > 0.6f ? GREEN : pct > 0.3f ? YELLOW : RED;
            drawRect(g, bx, by, bw, bh, withAlpha(BLACK, 200));
            if ((int) (bw * pct) > 0) drawRect(g, bx, by, (int) (bw * pct), bh, withAlpha(c, 255));
        }
    }

    public static void enableScissor(int x, int y, int w, int h) {
        RenderSystem.enableScissor(x, mc.getWindow().getHeight() - (y + h), w, h);
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }
}
