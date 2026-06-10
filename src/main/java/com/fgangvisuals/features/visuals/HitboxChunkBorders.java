package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientChatEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

public class HitboxChunkBorders {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (mc.player == null) return;
        if (Config.HITBOX_ENABLED.get()) {
            mc.getEntityRenderDispatcher().setRenderHitBoxes(true);
        }
        if (Config.CHUNK_BORDERS_ENABLED.get()) {
            mc.debugRenderer.renderChunkBorder(mc.player);
        }
    }
}
