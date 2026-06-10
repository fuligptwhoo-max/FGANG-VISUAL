package com.fgangvisuals.mixin.client;

import com.fgangvisuals.FGANGVisuals;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
    @Inject(method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void onAdd(Particle particle, CallbackInfo ci) {
        if (FGANGVisuals.getInstance() != null && !FGANGVisuals.getInstance().particleOptimizer.shouldSpawn()) {
            ci.cancel();
        }
    }
}
