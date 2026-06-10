package com.fgangvisuals.features.optimizations;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;
import org.lwjgl.opengl.GL11;

public class RenderOptimizer {
    private final Minecraft mc = Minecraft.getInstance();
    private long lastCheck = 0;

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.RENDER_OPTIMIZER.get()) return;
        long now = System.currentTimeMillis();
        if (now - lastCheck < 10000) return;
        lastCheck = now;
        int fps = mc.getFps();
        if (fps < 30 && Config.FAST_RENDER.get()) {
            mc.options.graphicsMode().set(net.minecraft.client.GraphicsStatus.FAST);
        }
    }

    @SubscribeEvent
    public void onRenderStart(RenderFrameEvent.Pre event) {
        if (Config.FAST_RENDER.get()) GL11.glDisable(GL11.GL_FOG);
    }
}
