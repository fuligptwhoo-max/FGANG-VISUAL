package com.fgangvisuals.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class CategoryPanel {
    private final Minecraft mc = Minecraft.getInstance();
    private final String name;
    private final int x, y, w, h, index;
    private float hover;

    public CategoryPanel(String name, int x, int y, int w, int h, int index) {
        this.name = name; this.x = x; this.y = y; this.w = w; this.h = h; this.index = index;
    }

    public void render(GuiGraphics g, int mx, int my, boolean selected, float anim) {
        boolean over = isMouseOver(mx, my);
        float target = over ? 1f : 0f;
        hover = hover + (target - hover) * 0.2f;

        int offX = (int) ((1 - anim) * -30);
        int rx = x + offX;

        int bg = selected ? 0xFF252525 : mix(0xFF1A1A1A, 0xFF222222, hover);
        int border = selected ? 0xFFFFFFFF : 0xFF333333;

        g.fill(rx, y, rx + w, y + h, bg);
        if (selected) g.fill(rx, y, rx + 2, y + h, 0xFFFFFFFF);
        g.fill(rx, y, rx + w, y + 1, border);
        g.fill(rx, y + h - 1, rx + w, y + h, border);
        g.fill(rx, y, rx + 1, y + h, border);
        g.fill(rx + w - 1, y, rx + w, y + h, border);

        String text = "§l" + name;
        int tc = selected ? 0xFFFFFFFF : (over ? 0xFFFFFFFF : 0xAAAAAA);
        g.drawString(mc.font, text, rx + 8, y + h / 2 - 4, tc, false);
    }

    public boolean isMouseOver(int mx, int my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    private int mix(int a, int b, float f) {
        int r = (int) (((a >> 16) & 0xFF) * (1 - f) + ((b >> 16) & 0xFF) * f);
        int g = (int) (((a >> 8) & 0xFF) * (1 - f) + ((b >> 8) & 0xFF) * f);
        int bl = (int) ((a & 0xFF) * (1 - f) + (b & 0xFF) * f);
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }
}
