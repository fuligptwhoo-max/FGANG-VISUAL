package com.fgangvisuals.event.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fgangvisuals.event.Event;

@Getter
@AllArgsConstructor
public class EventKeyInput extends Event {
    private final int key, action;
}