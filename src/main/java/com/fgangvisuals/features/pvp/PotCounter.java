package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class PotCounter {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.POT_COUNTER_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        int count = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.player.getInventory().getItem(i);
            if (s.is(Items.SPLASH_POTION) || s.is(Items.LINGERING_POTION)) {
                count++;
            }
        }
        if (count == 0) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.POT_COUNTER_SCALE.get().floatValue();
        int bx = Config.POT_COUNTER_X.get();
        int by = Config.POT_COUNTER_Y.get();
        int c = Config.POT_COUNTER_COLOR.get();

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        String t = count + " pots";
        int w = mc.font.width(t) + 8;
        RenderUtils.drawMinimalBackground(g, x, y, w, 14, 0.5f);
        g.drawString(mc.font, t, x + 4, y + 3, c, true);
        g.pose().popMatrix();
    }
}
