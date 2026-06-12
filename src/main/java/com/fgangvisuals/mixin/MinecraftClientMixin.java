package com.fgangvisuals.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.Window;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.fgangvisuals.event.EventGameUpdate;
import com.fgangvisuals.event.list.EventMinecraftInit;
import com.fgangvisuals.event.list.EventTick;
import com.fgangvisuals.event.list.EventTickEnd;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private Window window;

    @Unique
    private long lastHookTime = Util.getMeasuringTimeNano();
    @Unique
    private int accumulatedCalls = 0;

    @Unique
    private static final String TITLE_TEXT = "Чекушка Visuals";

    @Unique
    private static final long SHIMMER_CYCLE = 3000L;

    @Unique
    private static final long SHIMMER_OFF = 120L;

    @Unique
    private long titleAnimStart = -1L;

    @Inject(method = "getWindowTitle", at = @At(value = "HEAD"), cancellable = true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Чекушка Visuals");
    }

    @Inject(method = "render", at = @At(value = "RETURN"))
    private void onRenderEnd(boolean tick, CallbackInfo ci) {
        if (titleAnimStart == -1L) titleAnimStart = System.currentTimeMillis();

        long now = System.currentTimeMillis() - titleAnimStart;
        long mod = now % SHIMMER_CYCLE;
        int len = TITLE_TEXT.length();
        long stagger = SHIMMER_CYCLE / len;

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            long local = (mod - i * stagger + SHIMMER_CYCLE * 100L) % SHIMMER_CYCLE;
            sb.append(local < SHIMMER_OFF ? ' ' : TITLE_TEXT.charAt(i));
        }

        window.setTitle(sb.toString());
    }

    @Inject(method = "<init>", at = @At(value = "NEW", target = "(Lnet/minecraft/client/MinecraftClient;Ljava/io/File;)Lnet/minecraft/client/option/GameOptions;"))
    private void initOptions(RunArgs args, CallbackInfo ci) {
        new EventMinecraftInit().post();
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        new EventTick().post();
    }

    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void tickEnd(CallbackInfo ci) {
        new EventTickEnd().post();
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void render(boolean tick, CallbackInfo ci) {
        var timeNano = Util.getMeasuringTimeNano();
        var deltaTime = timeNano - lastHookTime;
        accumulatedCalls += (int) (deltaTime / 4_166_666L);
        lastHookTime += (long) accumulatedCalls * 4_166_666L;

        for (accumulatedCalls = Math.min(accumulatedCalls, 240); accumulatedCalls > 0; --accumulatedCalls) new EventGameUpdate().post();
    }
}
