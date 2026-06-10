package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.joml.Vector3f;

import java.util.Random;

public class HitParticles {
    private final Minecraft mc = Minecraft.getInstance();
    private final Random rand = new Random();

    @SubscribeEvent
    public void onDamage(LivingDamageEvent.Post event) {
        if (!Config.HIT_PARTICLES_ENABLED.get()) return;
        LivingEntity e = event.getEntity();
        if (!e.level().isClientSide()) return;
        if (Config.HIT_PARTICLES_ONLY_PLAYER.get() && event.getSource().getEntity() != mc.player) return;

        ClientLevel lvl = mc.level;
        if (lvl == null) return;
        int count = Config.HIT_PARTICLE_COUNT.get();
        int col = Config.HIT_PARTICLE_COLOR.get();
        float r = ((col >> 16) & 0xFF) / 255f;
        float g = ((col >> 8) & 0xFF) / 255f;
        float b = (col & 0xFF) / 255f;
        float size = Config.HIT_PARTICLE_SIZE.get().floatValue();

        for (int i = 0; i < count; i++) {
            double ox = (rand.nextDouble() - 0.5) * e.getBbWidth();
            double oy = rand.nextDouble() * e.getBbHeight();
            double oz = (rand.nextDouble() - 0.5) * e.getBbWidth();
            double vx = (rand.nextDouble() - 0.5) * 0.3;
            double vy = rand.nextDouble() * 0.3;
            double vz = (rand.nextDouble() - 0.5) * 0.3;
            lvl.addParticle(new DustParticleOptions(new Vector3f(r, g, b), size),
                e.getX() + ox, e.getY() + oy, e.getZ() + oz, vx, vy, vz);
        }

        if (event.getNewDamage() > 5f) {
            for (int i = 0; i < 5; i++) {
                lvl.addParticle(new DustParticleOptions(new Vector3f(1f, 0.9f, 0.3f), 1.2f),
                    e.getX(), e.getY() + e.getBbHeight() * 0.5, e.getZ(),
                    (rand.nextDouble() - 0.5) * 0.2, 0.1, (rand.nextDouble() - 0.5) * 0.2);
            }
        }
    }
}
