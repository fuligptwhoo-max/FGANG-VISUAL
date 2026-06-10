package com.fgangvisuals.mixin.client;

import com.fgangvisuals.FGANGVisuals;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    private void onShouldRender(Entity entity, Frustum frustum, double camX, double camY, double camZ,
                                CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (FGANGVisuals.getInstance() != null && !FGANGVisuals.getInstance().entityCulling.shouldRender(entity)) {
            cir.setReturnValue(false);
        }
    }
}
