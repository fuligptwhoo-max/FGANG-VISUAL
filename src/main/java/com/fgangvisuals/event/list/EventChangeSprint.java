package com.fgangvisuals.event.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fgangvisuals.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class EventChangeSprint extends Event {
    private boolean sprinting;
}