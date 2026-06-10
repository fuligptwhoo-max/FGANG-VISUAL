package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class ReachDisplay {
    private final Minecraft mc = Minecraft.getInstance();
    private double lastReach = 0;
    private long lastHit = 0;

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.REACH_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        if (mc.crosshairPickEntity instanceof Entity e) {
            double dist = mc.player.distanceTo(e);
            if (mc.options.keyAttack.isDown()) {
                lastReach = dist;
                lastHit = System.currentTimeMillis();
            }
        }

        if (System.currentTimeMillis() - lastHit > 2000) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.REACH_SCALE.get().floatValue();
        int bx = Config.REACH_X.get();
        int by = Config.REACH_Y.get();
        int c = Config.REACH_COLOR.get();
        int dec = Config.REACH_DECIMALS.get();

        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        String fmt = "%." + dec + "f blocks";
        String t = String.format(fmt, lastReach);
        int w = mc.font.width(t) + 8;
        if (Config.REACH_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, c, true);
        g.pose().popPose();
    }
}
