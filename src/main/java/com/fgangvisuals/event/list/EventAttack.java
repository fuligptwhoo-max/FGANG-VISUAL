package com.fgangvisuals.event.list;

import lombok.Getter;
import net.minecraft.entity.Entity;
import com.fgangvisuals.event.Event;

@Getter
public class EventAttack extends Event {
    private final Entity entity;
    private final boolean crit;

    public EventAttack(Entity entity, boolean crit) {
        this.entity = entity;
        this.crit = crit;
    }
}