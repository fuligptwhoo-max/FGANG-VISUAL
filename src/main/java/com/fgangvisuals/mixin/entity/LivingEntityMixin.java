package com.fgangvisuals.mixin.entity;

import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.config.Config;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "getHurtColor", at = @At("RETURN"), cancellable = true)
    private void onGetHurtColor(float partialTick, CallbackInfoReturnable<Integer> cir) {
        if (FGANGVisuals.getInstance() != null && Config.HIT_COLOR_ENABLED.get()) {
            LivingEntity self = (LivingEntity) (Object) this;
            int newColor = FGANGVisuals.getInstance().hitColor.modifyColor(self.getUUID(), cir.getReturnValue());
            if (newColor != cir.getReturnValue()) cir.setReturnValue(newColor);
        }
    }
}
