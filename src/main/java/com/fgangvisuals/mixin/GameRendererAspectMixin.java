package com.fgangvisuals.mixin;

import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.fgangvisuals.module.list.render.AspectRatio;

@Mixin(GameRenderer.class)
public class GameRendererAspectMixin {

    @Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"), cancellable = true)
    private void onGetBasicProjectionMatrix(float fov, CallbackInfoReturnable<Matrix4f> cir) {
        AspectRatio aspectRatio = AspectRatio.getInstance();
        if (aspectRatio != null && aspectRatio.isEnabled()) {
            float mult = aspectRatio.getAspectMultiplier();
            if (mult != 1.0f) {
                Matrix4f modified = new Matrix4f(cir.getReturnValue());
                modified.m00(modified.m00() * mult);
                cir.setReturnValue(modified);
            }
        }
    }
}
