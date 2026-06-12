package com.fgangvisuals.util.math;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.event.list.EventPacket;
import com.fgangvisuals.event.list.EventTick;
import com.fgangvisuals.util.IMinecraft;

@Getter
public class PingGetter implements IMinecraft {
    public PingGetter() {
        FGANGVisuals.getInstance().getEventBus().register(this);
    }

    private final StopWatch stopWatch = new StopWatch();
    private boolean lagged;
    private int ping;

    @Subscribe
    private void onUpdate(EventTick e) {
        ping = (int) stopWatch.getTime();
        if (stopWatch.getTime() > 1000) lagged = true;
    }

    @Subscribe
    private void onPacket(EventPacket e) {
        if (e.getPacket() instanceof CommonPingS2CPacket) {
            stopWatch.reset();
        }
    }
}