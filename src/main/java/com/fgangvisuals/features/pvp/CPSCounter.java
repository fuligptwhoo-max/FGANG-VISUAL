package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class CPSCounter {
    private final Minecraft mc = Minecraft.getInstance();
    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();

    @SubscribeEvent
    public void onClick(InputEvent.MouseButton.Pre event) {
        long now = System.currentTimeMillis();
        if (event.getButton() == 0) leftClicks.add(now);
        else if (event.getButton() == 1) rightClicks.add(now);
        cleanup();
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.CPS_ENABLED.get() || mc.player == null || mc.options.hideGui) return;
        cleanup();

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.CPS_SCALE.get().floatValue();
        int bx = Config.CPS_X.get();
        int by = Config.CPS_Y.get();
        int c = Config.CPS_COLOR.get();

        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);

        if (Config.CPS_COMBINED.get()) {
            String t = leftClicks.size() + " CPS";
            int w = mc.font.width(t) + 8;
            if (Config.CPS_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
            g.drawString(mc.font, t, x + 4, y + 3, c, true);
        } else {
            String l = "L: " + leftClicks.size();
            String r = "R: " + rightClicks.size();
            int w = Math.max(mc.font.width(l), mc.font.width(r)) + 8;
            int h = 28;
            if (Config.CPS_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, h, 0.5f);
            g.drawString(mc.font, l, x + 4, y + 4, c, true);
            g.drawString(mc.font, r, x + 4, y + 16, c, true);
        }
        g.pose().popPose();
    }

    private void cleanup() {
        long cut = System.currentTimeMillis() - 1000;
        leftClicks.removeIf(t -> t < cut);
        rightClicks.removeIf(t -> t < cut);
    }

    public int getLeft() { cleanup(); return leftClicks.size(); }
    public int getRight() { cleanup(); return rightClicks.size(); }
}
