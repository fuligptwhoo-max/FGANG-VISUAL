package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class SessionInfo {
    private final Minecraft mc = Minecraft.getInstance();
    private int kills = 0;
    private int deaths = 0;
    private final long sessionStart = System.currentTimeMillis();

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (mc.player == null) return;
        if (event.getEntity() == mc.player) deaths++;
        else if (event.getSource().getEntity() == mc.player) kills++;
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.SESSION_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.SESSION_SCALE.get().floatValue();
        int bx = Config.SESSION_X.get();
        int by = Config.SESSION_Y.get();
        int c = Config.SESSION_COLOR.get();

        java.util.List<String> lines = new java.util.ArrayList<>();
        if (Config.SESSION_SHOW_KILLS.get()) lines.add("Kills: " + kills);
        if (Config.SESSION_SHOW_DEATHS.get()) lines.add("Deaths: " + deaths);
        if (Config.SESSION_SHOW_KD.get()) lines.add("K/D: " + (deaths == 0 ? kills : String.format("%.2f", (double)kills/deaths)));
        if (Config.SESSION_SHOW_TIME.get()) {
            long mins = (System.currentTimeMillis() - sessionStart) / 60000;
            lines.add("Time: " + mins + "m");
        }
        if (lines.isEmpty()) return;

        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        int mw = 0;
        for (String s : lines) mw = Math.max(mw, mc.font.width(s));
        int w = mw + 8;
        int h = lines.size() * 12 + 4;
        if (Config.SESSION_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, h, 0.5f);
        for (int i = 0; i < lines.size(); i++) {
            g.drawString(mc.font, lines.get(i), x + 4, y + 4 + i * 12, c, true);
        }
        g.pose().popPose();
    }
}
