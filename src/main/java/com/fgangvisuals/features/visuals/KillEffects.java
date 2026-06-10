package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.joml.Vector3f;

import java.util.Random;

public class KillEffects {
    private final Minecraft mc = Minecraft.getInstance();
    private final Random rand = new Random();

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (!Config.KILL_EFFECTS_ENABLED.get()) return;
        LivingEntity e = event.getEntity();
        if (!e.level().isClientSide()) return;
        if (Config.KILL_EFFECTS_ONLY_PLAYER.get() && event.getSource().getEntity() != mc.player) return;

        Vec3 pos = e.position();
        int style = Config.KILL_EFFECT_STYLE.get();
        switch (style) {
            case 0 -> lightning(pos);
            case 1 -> explosion(pos);
            case 2 -> blood(pos, e.getBbHeight());
            case 3 -> digital(pos, e.getBbHeight());
        }
    }

    private void lightning(Vec3 p) {
        ClientLevel l = mc.level; if (l == null) return;
        for (int i = 0; i < 20; i++) {
            l.addParticle(ParticleTypes.END_ROD, p.x + (rand.nextDouble()-0.5)*2, p.y + rand.nextDouble()*3, p.z + (rand.nextDouble()-0.5)*2, 0, 0.3, 0);
        }
        if (Config.KILL_EFFECT_SOUND.get())
            l.playLocalSound(p.x, p.y, p.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.AMBIENT, 0.5f, 1f, false);
    }

    private void explosion(Vec3 p) {
        ClientLevel l = mc.level; if (l == null) return;
        l.addParticle(ParticleTypes.EXPLOSION, p.x, p.y + 1, p.z, 0, 0, 0);
        for (int i = 0; i < 30; i++)
            l.addParticle(ParticleTypes.FLAME, p.x, p.y + 0.5, p.z, (rand.nextDouble()-0.5)*0.5, rand.nextDouble()*0.5, (rand.nextDouble()-0.5)*0.5);
    }

    private void blood(Vec3 p, double h) {
        ClientLevel l = mc.level; if (l == null) return;
        Vector3f c = new Vector3f(0.8f, 0.1f, 0.1f);
        for (int i = 0; i < 25; i++)
            l.addParticle(new DustParticleOptions(c, 1.5f), p.x + (rand.nextDouble()-0.5)*0.5, p.y + rand.nextDouble()*h, p.z + (rand.nextDouble()-0.5)*0.5,
                (rand.nextDouble()-0.5)*0.3, rand.nextDouble()*0.2, (rand.nextDouble()-0.5)*0.3);
    }

    private void digital(Vec3 p, double h) {
        ClientLevel l = mc.level; if (l == null) return;
        int col = Config.KILL_EFFECT_COLOR.get();
        float r = ((col >> 16) & 0xFF) / 255f, g = ((col >> 8) & 0xFF) / 255f, b = (col & 0xFF) / 255f;
        for (int i = 0; i < 20; i++)
            l.addParticle(new DustParticleOptions(new Vector3f(r, g, b), 1.2f), p.x + (rand.nextDouble()-0.5)*0.8, p.y + rand.nextDouble()*h, p.z + (rand.nextDouble()-0.5)*0.8,
                0, rand.nextDouble()*0.5 + 0.2, 0);
        for (int i = 0; i < 16; i++) {
            double a = Math.PI * 2 * i / 16;
            l.addParticle(new DustParticleOptions(new Vector3f(0f, 0.94f, 1f), 1f), p.x, p.y + 0.1, p.z, Math.cos(a)*0.3, 0.1, Math.sin(a)*0.3);
        }
    }
}
