package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class MemoryDisplay {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.MEMORY_DISPLAY_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.MEMORY_DISPLAY_SCALE.get().floatValue();
        int bx = Config.MEMORY_DISPLAY_X.get();
        int by = Config.MEMORY_DISPLAY_Y.get();
        int c = Config.MEMORY_DISPLAY_COLOR.get();

        long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long max = Runtime.getRuntime().maxMemory();
        String t = String.format("%.1f/%.1f MB", used / 1048576f, max / 1048576f);

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        int w = mc.font.width(t) + 8;
        if (Config.MEMORY_DISPLAY_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, c, true);
        g.pose().popMatrix();
    }
}
