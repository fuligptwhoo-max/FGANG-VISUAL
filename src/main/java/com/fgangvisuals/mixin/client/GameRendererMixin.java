package com.fgangvisuals.mixin.client;

import com.fgangvisuals.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void onBobView(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        if (Config.REDUCED_BOBBING_ENABLED.get()) {
            float amt = Config.VIEW_BOBBING_AMOUNT.get().floatValue();
            if (amt <= 0.01f) ci.cancel();
        }
    }

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void onBobHurt(PoseStack poseStack, float partialTicks, CallbackInfo ci) {
        if (Config.NO_HURT_CAM_ENABLED.get()) {
            ci.cancel();
        }
    }
}
