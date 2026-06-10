package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public class WTapIndicator {
    private final Minecraft mc = Minecraft.getInstance();
    private long lastAttack = 0;
    private boolean shouldWTap = false;

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getEntity() != mc.player) return;
        shouldWTap = true;
        lastAttack = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.WTAP_ENABLED.get() || mc.player == null || mc.options.hideGui) return;
        if (System.currentTimeMillis() - lastAttack > 500) shouldWTap = false;
        if (!shouldWTap) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.WTAP_SCALE.get().floatValue();
        int bx = Config.WTAP_X.get();
        int by = Config.WTAP_Y.get();

        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        String t = "W-TAP";
        int w = mc.font.width(t) + 8;
        RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, RenderUtils.YELLOW, true);
        g.pose().popPose();
    }
}
