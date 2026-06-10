package com.fgangvisuals.mixin.entity;

import com.fgangvisuals.FGANGVisuals;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "baseTick", at = @At("HEAD"), cancellable = true)
    private void onBaseTick(CallbackInfo ci) {
        if (FGANGVisuals.getInstance() != null) {
            Entity self = (Entity) (Object) this;
            if (!FGANGVisuals.getInstance().entityOptimizer.shouldUpdate(self)) {
                ci.cancel();
            }
        }
    }
}
