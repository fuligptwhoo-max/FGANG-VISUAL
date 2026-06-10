package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class ArmorHUD {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.ARMOR_HUD_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.ARMOR_HUD_SCALE.get().floatValue();
        int baseX = Config.ARMOR_HUD_X.get();
        int baseY = Config.ARMOR_HUD_Y.get();
        boolean vert = Config.ARMOR_HUD_VERTICAL.get();
        int color = Config.ARMOR_HUD_COLOR.get();

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        int x = (int) (baseX / scale);
        int y = (int) (baseY / scale);

        int slotSize = 20;
        int bgW = vert ? slotSize + 4 : slotSize * 4 + 6;
        int bgH = vert ? slotSize * 4 + 6 : slotSize + 4;

        if (Config.ARMOR_HUD_BACKGROUND.get()) {
            RenderUtils.drawMinimalBackground(g, x, y, bgW, bgH, Config.ARMOR_HUD_BG_ALPHA.get().floatValue());
        }

        ItemStack[] armor = {
            mc.player.getItemBySlot(EquipmentSlot.HEAD),
            mc.player.getItemBySlot(EquipmentSlot.CHEST),
            mc.player.getItemBySlot(EquipmentSlot.LEGS),
            mc.player.getItemBySlot(EquipmentSlot.FEET)
        };

        for (int i = 0; i < 4; i++) {
            int sx = vert ? x + 2 : x + 2 + i * (slotSize + 2);
            int sy = vert ? y + 2 + i * (slotSize + 2) : y + 2;
            g.fill(sx, sy, sx + slotSize, sy + slotSize, 0x22000000);
            RenderUtils.drawBorder(g, sx, sy, slotSize, slotSize, RenderUtils.withAlphaF(color, 0.3f));

            ItemStack stack = armor[i];
            if (!stack.isEmpty()) {
                g.renderItem(stack, sx + 2, sy + 2);
                if (stack.isDamageableItem()) {
                    int max = stack.getMaxDamage();
                    int cur = max - stack.getDamageValue();
                    float pct = (float) cur / max;
                    int bw = slotSize - 4;
                    int bh = 2;
                    int bx = sx + 2;
                    int by = sy + slotSize - 4;
                    int c = pct > 0.6f ? RenderUtils.GREEN : pct > 0.3f ? RenderUtils.YELLOW : RenderUtils.RED;
                    g.fill(bx, by, bx + bw, by + bh, 0xFF000000);
                    g.fill(bx, by, bx + (int)(bw * pct), by + bh, c);

                    if (Config.ARMOR_HUD_DURABILITY.get()) {
                        String txt = Config.ARMOR_HUD_PERCENTAGE.get() ? (int)(pct*100)+"%" : String.valueOf(cur);
                        g.drawString(mc.font, txt, sx + slotSize + 2, sy + 6, c, true);
                    }
                }
            } else {
                g.drawString(mc.font, "—", sx + 6, sy + 6, 0x444444, false);
            }
        }
        g.pose().popMatrix();
    }
}
