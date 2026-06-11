package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

public class Zoom {
    private final Minecraft mc = Minecraft.getInstance();
    public static KeyMapping zoomKey;
    private int currentFov = 0;
    private boolean wasZooming = false;

    public void registerKey(RegisterKeyMappingsEvent event) {
        zoomKey = new KeyMapping("key.fgangvisuals.zoom", GLFW.GLFW_KEY_C, "key.categories.fgangvisuals");
        event.register(zoomKey);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.ZOOM_ENABLED.get() || mc.player == null) return;
        if (zoomKey == null) return;

        boolean zooming = zoomKey.isDown();
        if (zooming && !wasZooming) {
            currentFov = mc.options.fov().get();
        }
        if (zooming) {
            float target = Config.ZOOM_FOV.get().floatValue();
            if (Config.ZOOM_SMOOTH.get()) {
                int current = mc.options.fov().get();
                mc.options.fov().set((int)(current + (target - current) * 0.1f));
            } else {
                mc.options.fov().set((int)target);
            }
        } else if (wasZooming) {
            if (Config.ZOOM_SMOOTH.get()) {
                int current = mc.options.fov().get();
                mc.options.fov().set((int)(current + (currentFov - current) * 0.1f));
            } else {
                mc.options.fov().set(currentFov);
            }
        }
        wasZooming = zooming;
    }
}
