package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

public class NoHurtCam {
    @SubscribeEvent
    public void onCamera(ViewportEvent.ComputeCameraAngles event) {
        if (!Config.NO_HURT_CAM_ENABLED.get()) return;
        event.setRoll(0);
    }
}
