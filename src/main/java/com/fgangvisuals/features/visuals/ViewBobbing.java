package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class ViewBobbing {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onCamera(ViewportEvent.ComputeCameraAngles event) {
        if (!Config.REDUCED_BOBBING_ENABLED.get()) return;
        float amt = Config.VIEW_BOBBING_AMOUNT.get().floatValue();
        if (mc.options.bobView().get()) {
            event.setYaw(event.getYaw() * amt);
            event.setPitch(event.getPitch() * amt);
        }
    }
}
