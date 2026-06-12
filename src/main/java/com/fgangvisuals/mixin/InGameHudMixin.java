package com.fgangvisuals.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.event.list.EventHUD;
import com.fgangvisuals.module.list.render.CustomCrosshair;
import com.fgangvisuals.util.base.Instance;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At(value = "RETURN"))
    private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        new EventHUD(context, tickCounter).post();
    }

    @Inject(method = "renderCrosshair", at = @At(value = "HEAD"), cancellable = true)
    private void cancelDefaultCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        CustomCrosshair crosshair = Instance.get(CustomCrosshair.class);
        if (crosshair != null && crosshair.isEnabled()) {
            ci.cancel();
        }
    }
}
