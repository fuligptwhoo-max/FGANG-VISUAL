package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

public class LowFire {
    @SubscribeEvent
    public void onFire(RenderBlockScreenEffectEvent event) {
        if (!Config.LOW_FIRE_ENABLED.get()) return;
        if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE) {
            // Scale fire overlay down via Mixin into Gui or InGameOverlayRenderer
            // Placeholder: cancel and draw custom lower fire
        }
    }
}
