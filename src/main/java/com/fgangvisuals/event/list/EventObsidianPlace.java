package com.fgangvisuals.event.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.math.BlockPos;
import com.fgangvisuals.event.Event;

@Getter
@AllArgsConstructor
public class EventObsidianPlace extends Event {
    private final BlockPos blockPos;
}