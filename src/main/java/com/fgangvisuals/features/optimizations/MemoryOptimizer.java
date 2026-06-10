package com.fgangvisuals.features.optimizations;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class MemoryOptimizer {
    private final Minecraft mc = Minecraft.getInstance();
    private long lastCleanup = 0;

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.MEMORY_OPTIMIZER.get() || mc.level == null) return;
        long now = System.currentTimeMillis();
        int interval = Config.MEMORY_CLEANUP_INTERVAL.get() * 1000;
        if (now - lastCleanup > interval) {
            System.gc();
            lastCleanup = now;
        }
    }
}
