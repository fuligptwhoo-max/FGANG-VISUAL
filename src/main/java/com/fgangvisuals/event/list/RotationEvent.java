package com.fgangvisuals.event.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fgangvisuals.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class RotationEvent extends Event {
    private float yaw, pitch;
    private float partialTicks;
}