package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;

public class EnvironmentEditor {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (mc.level == null) return;
        if (Config.NO_WEATHER_ENABLED.get()) {
            if (mc.level.isRaining() || mc.level.isThundering()) {
                mc.level.getLevelData().setRaining(false);
            }
        }
    }

    @SubscribeEvent
    public void onRenderLevel(RenderLevelStageEvent event) {
        if (!Config.NO_FOG_ENABLED.get()) return;
        // Fog removal is better handled via Mixin or RenderSystem
    }
}
