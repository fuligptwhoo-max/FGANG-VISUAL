package com.fgangvisuals.event.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import com.fgangvisuals.event.Event;

@Getter
@Setter
@AllArgsConstructor
public class FireworkEvent extends Event {
    private final LivingEntity boostedEntity;
    private float speed;
}