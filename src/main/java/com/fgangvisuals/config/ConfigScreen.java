package com.fgangvisuals.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.literal("FGANG Visuals - Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int bw = 200, bh = 20, sx = this.width / 2 - bw / 2, sy = 60, gap = 24;
        addRenderableWidget(Button.builder(Component.literal("PVP Features"), b -> {})
            .pos(sx, sy).size(bw, bh).build());
        addRenderableWidget(Button.builder(Component.literal("Visual Effects"), b -> {})
            .pos(sx, sy + gap).size(bw, bh).build());
        addRenderableWidget(Button.builder(Component.literal("Optimizations"), b -> {})
            .pos(sx, sy + gap * 2).size(bw, bh).build());
        addRenderableWidget(Button.builder(Component.literal("Crosshair"), b -> {})
            .pos(sx, sy + gap * 3).size(bw, bh).build());
        addRenderableWidget(Button.builder(Component.literal("Back"), b -> this.minecraft.setScreen(parent))
            .pos(sx, sy + gap * 5).size(bw, bh).build());
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        g.fill(0, 0, this.width, this.height, 0xFF111111);
        g.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);
        g.drawCenteredString(this.font, Component.literal("§7Minimalist visual mod"), this.width / 2, 35, 0x888888);
        super.render(g, mx, my, pt);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
