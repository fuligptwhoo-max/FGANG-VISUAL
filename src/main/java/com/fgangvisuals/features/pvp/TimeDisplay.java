package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeDisplay {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.TIME_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.TIME_SCALE.get().floatValue();
        int bx = Config.TIME_X.get();
        int by = Config.TIME_Y.get();
        int c = Config.TIME_COLOR.get();

        LocalTime now = LocalTime.now();
        String fmt = Config.TIME_24H.get() ? "HH:mm" : "hh:mm a";
        String t = now.format(DateTimeFormatter.ofPattern(fmt));

        g.pose().pushPose();
        g.pose().scale((float) scale, (float) scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        int w = mc.font.width(t) + 8;
        if (Config.TIME_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, c, true);
        g.pose().popPose();
    }
}
