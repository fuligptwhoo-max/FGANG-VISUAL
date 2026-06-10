package com.fgangvisuals.features.optimizations;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;

import java.util.HashMap;
import java.util.Map;

public class EntityOptimizer {
    private final Minecraft mc = Minecraft.getInstance();
    private final Map<Integer, Long> lastUpdate = new HashMap<>();
    private int tick = 0;

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.ENTITY_OPTIMIZER.get() || mc.level == null) return;
        tick++;
        long now = System.currentTimeMillis();
        if (tick % 200 == 0) lastUpdate.entrySet().removeIf(e -> now - e.getValue() > 5000);
    }

    public boolean shouldUpdate(Entity e) {
        if (!Config.ENTITY_OPTIMIZER.get()) return true;
        if (mc.player == null) return true;
        double max = Config.ENTITY_OPTIMIZER_DISTANCE.get();
        if (e.distanceTo(mc.player) > max) {
            Long t = lastUpdate.get(e.getId());
            if (t != null && System.currentTimeMillis() - t < 1000) return false;
            lastUpdate.put(e.getId(), System.currentTimeMillis());
        }
        return true;
    }

    public void clearCache() { lastUpdate.clear(); }
}
