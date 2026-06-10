package com.fgangvisuals.features.optimizations;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;

public class FPSSmooth {
    private final Minecraft mc = Minecraft.getInstance();
    private final MathUtils.MovingAverage avg = new MathUtils.MovingAverage(20);
    private float smoothed = 60f;
    private long last = 0;

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.FPS_SMOOTH.get()) return;
        long now = System.nanoTime();
        if (last != 0) {
            long dt = now - last;
            float instant = 1_000_000_000f / dt;
            avg.add(instant);
        }
        last = now;
        smoothed = MathUtils.smoothLerp(smoothed, avg.getAverage(), 0.1f);
    }

    public float getSmoothed() { return smoothed; }
    public void reset() { avg.reset(); smoothed = 60f; last = 0; }
}
