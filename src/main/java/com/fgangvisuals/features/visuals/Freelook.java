package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

public class Freelook {
    private final Minecraft mc = Minecraft.getInstance();
    public static KeyMapping freelookKey;
    private boolean active = false;
    private float yaw, pitch;

    public void registerKey(RegisterKeyMappingsEvent event) {
        freelookKey = new KeyMapping("key.fgangvisuals.freelook", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.fgangvisuals");
        event.register(freelookKey);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.FREELOOK_ENABLED.get() || mc.player == null) return;
        if (freelookKey == null) return;

        if (freelookKey.isDown() && !active) {
            active = true;
            yaw = mc.player.getYRot();
            pitch = mc.player.getXRot();
        }
        if (!freelookKey.isDown() && active) {
            active = false;
            if (Config.FREELOOK_RETURN_ON_RELEASE.get()) {
                mc.player.setYRot(yaw);
                mc.player.setXRot(pitch);
            }
        }
    }

    @SubscribeEvent
    public void onCamera(ViewportEvent.ComputeCameraAngles event) {
        if (!active) return;
        event.setYaw(event.getYaw());
        event.setPitch(event.getPitch());
    }

    public boolean isActive() { return active; }
}
