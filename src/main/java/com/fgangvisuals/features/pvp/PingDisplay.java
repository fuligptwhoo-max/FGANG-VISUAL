package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class PingDisplay {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.PING_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        PlayerInfo info = mc.player.connection.getPlayerInfo(mc.player.getUUID());
        if (info == null) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.PING_SCALE.get().floatValue();
        int bx = Config.PING_X.get();
        int by = Config.PING_Y.get();
        int ping = info.getLatency();
        int c = Config.PING_COLOR.get();

        if (Config.PING_COLORED.get()) {
            c = ping < 50 ? RenderUtils.GREEN : ping < 100 ? RenderUtils.WHITE : ping < 200 ? RenderUtils.YELLOW : RenderUtils.RED;
        }

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        String t = ping + " ms";
        int w = mc.font.width(t) + 8;
        if (Config.PING_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, c, true);
        g.pose().popMatrix();
    }
}
