package com.fgangvisuals.features.optimizations;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class ParticleOptimizer {
    private final Minecraft mc = Minecraft.getInstance();
    private int currentCount = 0;

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.PARTICLE_OPTIMIZER.get() || mc.level == null) return;
        if (mc.level.getGameTime() % 20 == 0) {
            try { currentCount = mc.particleEngine.countParticles(); } catch (Exception ignored) {}
        }
        int max = Config.MAX_PARTICLES.get();
        if (currentCount > max * 0.95) {
            if (mc.options.particles().get() == net.minecraft.client.ParticleStatus.ALL)
                mc.options.particles().set(net.minecraft.client.ParticleStatus.DECREASED);
        }
        int fps = mc.getFps();
        if (fps < 30 && mc.options.particles().get() == net.minecraft.client.ParticleStatus.ALL)
            mc.options.particles().set(net.minecraft.client.ParticleStatus.DECREASED);
        else if (fps > 60 && mc.options.particles().get() == net.minecraft.client.ParticleStatus.DECREASED)
            mc.options.particles().set(net.minecraft.client.ParticleStatus.ALL);
    }

    public boolean shouldSpawn() {
        if (!Config.PARTICLE_OPTIMIZER.get()) return true;
        return currentCount < Config.MAX_PARTICLES.get();
    }
}
