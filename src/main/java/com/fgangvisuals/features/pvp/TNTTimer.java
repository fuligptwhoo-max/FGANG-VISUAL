package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.item.PrimedTnt;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;

public class TNTTimer {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRenderNameTag(RenderNameTagEvent event) {
        if (!Config.TNT_TIMER_ENABLED.get()) return;
        if (event.getEntity() instanceof PrimedTnt tnt) {
            int fuse = tnt.getFuse();
            float secs = fuse / 20f;
            String text = String.format("%.1fs", secs);
            int c = secs < 1 ? RenderUtils.RED : Config.TNT_TIMER_COLOR.get();
            event.setContent(net.minecraft.network.chat.Component.literal(text));
        }
    }
}
