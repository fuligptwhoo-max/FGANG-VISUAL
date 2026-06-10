package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HitColor {
    private final Minecraft mc = Minecraft.getInstance();
    private final Map<UUID, Long> hitTimes = new HashMap<>();

    @SubscribeEvent
    public void onDamage(LivingDamageEvent.Post event) {
        if (!Config.HIT_COLOR_ENABLED.get()) return;
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide()) return;
        if (Config.HIT_COLOR_ONLY_PLAYER.get()) {
            if (event.getSource().getEntity() != mc.player) return;
        }
        hitTimes.put(entity.getUUID(), System.currentTimeMillis());
    }

    public int modifyColor(UUID id, int original) {
        if (!Config.HIT_COLOR_ENABLED.get()) return original;
        Long time = hitTimes.get(id);
        if (time == null) return original;
        if (System.currentTimeMillis() - time > Config.HIT_COLOR_DURATION.get()) {
            hitTimes.remove(id);
            return original;
        }
        return Config.HIT_COLOR_VALUE.get();
    }

    public void cleanup() {
        hitTimes.entrySet().removeIf(e -> System.currentTimeMillis() - e.getValue() > Config.HIT_COLOR_DURATION.get());
    }
}
