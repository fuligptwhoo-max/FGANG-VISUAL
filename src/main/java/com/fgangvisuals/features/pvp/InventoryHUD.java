package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class InventoryHUD {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.INV_HUD_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.INV_HUD_SCALE.get().floatValue();
        int bx = Config.INV_HUD_X.get();
        int by = Config.INV_HUD_Y.get();

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        int slots = 9;
        int size = 18;
        int bgW = slots * size + 4;
        int bgH = size + 4;
        if (Config.INV_HUD_SHOW_ARMOR.get()) bgH += size + 2;

        if (Config.INV_HUD_BACKGROUND.get()) {
            RenderUtils.drawMinimalBackground(g, x, y, bgW, bgH, 0.5f);
        }

        for (int i = 0; i < slots; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            int sx = x + 2 + i * size;
            int sy = y + 2;
            g.fill(sx, sy, sx + size - 2, sy + size - 2, 0x22000000);
            if (!stack.isEmpty()) g.renderItem(stack, sx + 1, sy + 1);
        }

        if (Config.INV_HUD_SHOW_ARMOR.get()) {
            int ay = y + size + 4;
            for (int i = 0; i < 4; i++) {
                ItemStack stack = mc.player.getItemBySlot(EquipmentSlot.values()[3 - i]);
                int sx = x + 2 + i * size;
                g.fill(sx, ay, sx + size - 2, ay + size - 2, 0x22000000);
                if (!stack.isEmpty()) g.renderItem(stack, sx + 1, ay + 1);
            }
        }
        g.pose().popMatrix();
    }
}
