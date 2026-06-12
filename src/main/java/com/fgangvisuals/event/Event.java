package com.fgangvisuals.event;

import lombok.Data;
import com.fgangvisuals.FGANGVisuals;

@Data
public class Event {
    private boolean cancelled;

    public void post() {
        FGANGVisuals.getInstance().getEventBus().post(this);
    }

    public void cancelEvent() {
        setCancelled(true);
    }
}