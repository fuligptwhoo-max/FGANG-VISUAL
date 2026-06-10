package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;

public class FullBright {
    private final Minecraft mc = Minecraft.getInstance();
    private double originalGamma = 1.0;
    private boolean wasEnabled = false;

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (mc.player == null) return;
        boolean enabled = Config.FULLBRIGHT_ENABLED.get();
        if (enabled && !wasEnabled) {
            originalGamma = mc.options.gamma().get();
            mc.options.gamma().set(Config.FULLBRIGHT_GAMMA.get());
        } else if (!enabled && wasEnabled) {
            mc.options.gamma().set(originalGamma);
        }
        if (enabled && mc.options.gamma().get() < Config.FULLBRIGHT_GAMMA.get()) {
            mc.options.gamma().set(Config.FULLBRIGHT_GAMMA.get());
        }
        wasEnabled = enabled;
    }

    public void restore() { mc.options.gamma().set(originalGamma); }
}
