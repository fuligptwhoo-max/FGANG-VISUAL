package com.fgangvisuals.features.pvp;

import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class CustomCrosshair {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.CROSSHAIR_ENABLED.get() || mc.player == null) return;
        if (mc.getDebugOverlay().showDebugScreen()) return;

        FGANGVisuals.getInstance().crosshairManager.render(event.getGuiGraphics());
    }
}
