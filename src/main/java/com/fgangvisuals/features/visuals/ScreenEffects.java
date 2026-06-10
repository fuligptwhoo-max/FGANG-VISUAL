package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class ScreenEffects {
    private final Minecraft mc = Minecraft.getInstance();
    private long lastHit = 0;
    private long lastKill = 0;

    @SubscribeEvent
    public void onDamage(LivingDamageEvent.Post event) {
        if (event.getEntity() == mc.player) {
            lastHit = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onKill(net.neoforged.neoforge.event.entity.living.LivingDeathEvent event) {
        if (event.getSource().getEntity() == mc.player) {
            lastKill = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (mc.player == null) return;
        GuiGraphics g = event.getGuiGraphics();
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        // Screen shake on hit
        if (Config.SCREEN_SHAKE_ENABLED.get()) {
            long dt = System.currentTimeMillis() - lastHit;
            if (dt < 300) {
                float amt = Config.SCREEN_SHAKE_AMOUNT.get().floatValue();
                int offX = (int) ((Math.random() - 0.5) * amt * 10);
                int offY = (int) ((Math.random() - 0.5) * amt * 10);
                // Actual shake would require pose stack manipulation
            }
        }

        // Screen flash on kill
        if (Config.SCREEN_FLASH_ENABLED.get()) {
            long dt = System.currentTimeMillis() - lastKill;
            if (dt < 200) {
                int col = Config.SCREEN_FLASH_COLOR.get();
                float alpha = 1f - (dt / 200f);
                g.fill(0, 0, sw, sh, RenderUtils.withAlphaF(col, alpha * 0.3f));
            }
        }
    }
}
