package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class FPSCounter {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.FPS_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.FPS_SCALE.get().floatValue();
        int bx = Config.FPS_X.get();
        int by = Config.FPS_Y.get();
        int c = Config.FPS_COLOR.get();

        int fps = mc.getFps();
        if (Config.FPS_COLORED.get()) {
            c = fps >= 120 ? RenderUtils.GREEN : fps >= 60 ? RenderUtils.WHITE : fps >= 30 ? RenderUtils.YELLOW : RenderUtils.RED;
        }

        g.pose().pushPose();
        g.pose().scale((float) scale, (float) scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        String t = fps + " FPS";
        int w = mc.font.width(t) + 8;
        if (Config.FPS_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, c, true);
        g.pose().popPose();
    }
}
