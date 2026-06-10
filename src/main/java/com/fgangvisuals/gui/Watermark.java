package com.fgangvisuals.gui;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class Watermark {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.WATERMARK_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.WATERMARK_SCALE.get().floatValue();
        int bx = Config.WATERMARK_X.get();
        int by = Config.WATERMARK_Y.get();
        int c = Config.WATERMARK_COLOR.get();
        String text = Config.WATERMARK_TEXT.get();

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        g.drawString(mc.font, text, (int)(bx/scale), (int)(by/scale), c, true);
        g.pose().popMatrix();
    }
}
