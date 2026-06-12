package com.fgangvisuals.event.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import com.fgangvisuals.event.Event;

@Getter
@AllArgsConstructor
public class EventPopTotem extends Event {
    private final PlayerEntity player;
}