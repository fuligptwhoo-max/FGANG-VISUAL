package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class ComboCounter {
    private final Minecraft mc = Minecraft.getInstance();
    private int combo = 0;
    private long lastComboTime = 0;
    private Entity lastTarget = null;

    @SubscribeEvent
    public void onDamage(LivingDamageEvent.Post event) {
        if (mc.player == null) return;
        LivingEntity victim = event.getEntity();
        if (victim == mc.player) {
            combo = 0;
            return;
        }
        if (event.getSource().getEntity() == mc.player) {
            if (lastTarget == victim && System.currentTimeMillis() - lastComboTime < Config.COMBO_RESET.get()) {
                combo++;
            } else {
                combo = 1;
            }
            lastTarget = victim;
            lastComboTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.COMBO_ENABLED.get() || mc.player == null || mc.options.hideGui) return;
        if (System.currentTimeMillis() - lastComboTime > Config.COMBO_RESET.get()) combo = 0;
        if (combo <= 0) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.COMBO_SCALE.get().floatValue();
        int bx = Config.COMBO_X.get();
        int by = Config.COMBO_Y.get();
        int c = Config.COMBO_COLOR.get();

        g.pose().pushPose();
        g.pose().scale((float) scale, (float) scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        String t = combo + "x Combo";
        int w = mc.font.width(t) + 8;
        if (Config.COMBO_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, c, true);
        g.pose().popPose();
    }
}
