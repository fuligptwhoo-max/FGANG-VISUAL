package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.Collection;

public class TargetHUD {
    private final Minecraft mc = Minecraft.getInstance();
    private LivingEntity target = null;
    private long lastHit = 0;

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.TARGET_HUD_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        // Update target from crosshair
        if (mc.crosshairPickEntity instanceof LivingEntity le && le != mc.player) {
            target = le;
            lastHit = System.currentTimeMillis();
        }
        if (target == null || target.isRemoved() || System.currentTimeMillis() - lastHit > 3000) {
            target = null;
            return;
        }

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.TARGET_HUD_SCALE.get().floatValue();
        int bx = Config.TARGET_HUD_X.get();
        int by = Config.TARGET_HUD_Y.get();
        int c = Config.TARGET_HUD_COLOR.get();

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);

        String name = target.getDisplayName().getString();
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();
        float pct = health / maxHealth;

        int w = 120;
        int h = 40;
        if (Config.TARGET_HUD_SHOW_POTIONS.get()) {
            Collection<MobEffectInstance> effects = target.getActiveEffects();
            if (!effects.isEmpty()) h += effects.size() * 12;
        }

        if (Config.TARGET_HUD_BACKGROUND.get()) {
            RenderUtils.drawMinimalBackground(g, x, y, w, h, 0.6f);
        }

        g.drawString(mc.font, name, x + 4, y + 4, c, true);

        if (Config.TARGET_HUD_SHOW_HEALTH.get()) {
            int barY = y + 16;
            g.fill(x + 4, barY, x + w - 4, barY + 6, 0xFF333333);
            int fill = (int) ((w - 8) * pct);
            int hc = pct > 0.6f ? RenderUtils.GREEN : pct > 0.3f ? RenderUtils.YELLOW : RenderUtils.RED;
            if (fill > 0) g.fill(x + 4, barY, x + 4 + fill, barY + 6, hc);
            String ht = String.format("%.1f / %.1f", health, maxHealth);
            g.drawString(mc.font, ht, x + 4, barY + 8, c, true);
        }

        if (Config.TARGET_HUD_SHOW_ARMOR.get() && target instanceof Player p) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = p.getInventory().getItem(36 + (3 - i));
                if (!stack.isEmpty()) {
                    g.renderItem(stack, x + 4 + i * 18, y + 30);
                }
            }
        }

        if (Config.TARGET_HUD_SHOW_POTIONS.get()) {
            int py = y + (Config.TARGET_HUD_SHOW_ARMOR.get() ? 50 : 32);
            for (MobEffectInstance eff : target.getActiveEffects()) {
                String txt = eff.getEffect().value().getDisplayName().getString() + " " + formatTime(eff.getDuration());
                g.drawString(mc.font, txt, x + 4, py, c, true);
                py += 12;
            }
        }

        g.pose().popMatrix();
    }

    private String formatTime(int ticks) {
        int s = ticks / 20;
        return String.format("%d:%02d", s / 60, s % 60);
    }
}
