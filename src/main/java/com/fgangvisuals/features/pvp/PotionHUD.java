package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotionHUD {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.POTION_HUD_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        Collection<MobEffectInstance> effects = mc.player.getActiveEffects();
        if (effects.isEmpty()) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.POTION_HUD_SCALE.get().floatValue();
        int bx = Config.POTION_HUD_X.get();
        int by = Config.POTION_HUD_Y.get();
        boolean icons = Config.POTION_HUD_ICONS.get();
        int color = Config.POTION_HUD_COLOR.get();

        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);

        List<MobEffectInstance> sorted = new ArrayList<>(effects);
        sorted.sort((a, b) -> Integer.compare(b.getDuration(), a.getDuration()));

        int rowH = icons ? 22 : 14;
        int bgW = 130;
        int bgH = sorted.size() * rowH + 4;

        if (Config.POTION_HUD_BACKGROUND.get()) {
            RenderUtils.drawMinimalBackground(g, x, y, bgW, bgH, 0.5f);
        }

        for (int i = 0; i < sorted.size(); i++) {
            MobEffectInstance eff = sorted.get(i);
            int ry = y + 2 + i * rowH;
            int ec = eff.getEffect().value().getColor();
            g.fill(x + 2, ry, x + 4, ry + rowH - 2, 0xFF000000 | ec);

            if (icons) {
                Holder<MobEffect> holder = eff.getEffect();
                var sprite = mc.getMobEffectTextures().get(holder);
                g.blit(x + 8, ry, 0, 18, 18, sprite);
            }

            String name = eff.getEffect().value().getDisplayName().getString();
            int amp = eff.getAmplifier();
            String txt = name + (amp > 0 ? " " + (amp + 1) : "");
            int tx = icons ? x + 30 : x + 10;
            g.drawString(mc.font, txt, tx, ry + 2, color, true);

            String time = formatTime(eff.getDuration());
            int tc = eff.getDuration() < 200 ? RenderUtils.RED : eff.getDuration() < 600 ? RenderUtils.YELLOW : color;
            g.drawString(mc.font, time, tx, ry + (icons ? 12 : 10), tc, true);
        }
        g.pose().popPose();
    }

    private String formatTime(int ticks) {
        int s = ticks / 20;
        int m = s / 60;
        int h = m / 60;
        if (h > 0) return String.format("%d:%02d:%02d", h, m % 60, s % 60);
        return String.format("%d:%02d", m % 60, s % 60);
    }
}
