package com.fgangvisuals.mixin.client;

import com.fgangvisuals.FGANGVisuals;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow public Frustum cullingFrustum;

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void onRenderEntity(Entity entity, double camX, double camY, double camZ, float partialTick,
                                com.mojang.blaze3d.vertex.PoseStack poseStack,
                                net.minecraft.client.renderer.MultiBufferSource bufferSource,
                                CallbackInfo ci) {
        if (FGANGVisuals.getInstance() != null && !FGANGVisuals.getInstance().entityCulling.shouldRender(entity)) {
            ci.cancel();
        }
    }
}
