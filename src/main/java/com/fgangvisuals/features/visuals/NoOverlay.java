package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class NoOverlay {
    @SubscribeEvent
    public void onBlockOverlay(RenderBlockScreenEffectEvent event) {
        switch (event.getOverlayType()) {
            case FIRE -> { if (Config.NO_OVERLAY_FIRE.get()) event.setCanceled(true); }
            case BLOCK -> { if (Config.NO_OVERLAY_PUMPKIN.get()) event.setCanceled(true); }
        }
    }

    @SubscribeEvent
    public void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (Config.NO_OVERLAY_NAUSEA.get()) {
            event.setRoll(0);
        }
    }
}
