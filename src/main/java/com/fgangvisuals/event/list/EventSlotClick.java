package com.fgangvisuals.event.list;

import lombok.Getter;
import net.minecraft.screen.slot.Slot;
import com.fgangvisuals.event.Event;

@Getter
public class EventSlotClick extends Event {
    private final Slot slot;
    private final int button;

    public EventSlotClick(Slot slot, int button) {
        this.slot = slot;
        this.button = button;
    }
}
