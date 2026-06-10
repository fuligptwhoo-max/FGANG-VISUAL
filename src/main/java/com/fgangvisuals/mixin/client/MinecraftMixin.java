package com.fgangvisuals.mixin.client;

import com.fgangvisuals.FGANGVisuals;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V", at = @At("RETURN"))
    private void onDisconnect(CallbackInfo ci) {
        FGANGVisuals v = FGANGVisuals.getInstance();
        if (v != null) {
            v.trailEffect.clear();
            v.entityCulling.clear();
            v.entityOptimizer.clearCache();
        }
    }
}
