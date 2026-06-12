package com.fgangvisuals.module.list.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import com.fgangvisuals.event.list.EventTick;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.list.render.hud.Interface;
import com.fgangvisuals.module.settings.SliderSetting;

import java.util.HashSet;
import java.util.Set;

@ModuleInformation(moduleName = "Buff Notification", moduleDesc = "Уведомление когда бафф скоро кончится", moduleCategory = ModuleCategory.PLAYER)
public class BuffNotification extends Module {

    private final SliderSetting threshold = new SliderSetting("Порог секунд", 5.0f, 1.0f, 30.0f, 0.5f);

    private static final Set<RegistryEntry<StatusEffect>> USEFUL_BUFFS = Set.of(
            StatusEffects.SPEED,
            StatusEffects.STRENGTH,
            StatusEffects.REGENERATION,
            StatusEffects.FIRE_RESISTANCE,
            StatusEffects.RESISTANCE,
            StatusEffects.ABSORPTION,
            StatusEffects.HASTE,
            StatusEffects.JUMP_BOOST,
            StatusEffects.NIGHT_VISION,
            StatusEffects.INVISIBILITY,
            StatusEffects.WATER_BREATHING,
            StatusEffects.DOLPHINS_GRACE,
            StatusEffects.SLOW_FALLING
    );

    private final Set<RegistryEntry<StatusEffect>> warned = new HashSet<>();

    @Subscribe
    private void onTick(EventTick e) {
        if (mc.player == null) return;

        float thresholdSec = threshold.getFloatValue();
        Set<RegistryEntry<StatusEffect>> active = new HashSet<>();

        for (StatusEffectInstance instance : mc.player.getStatusEffects()) {
            RegistryEntry<StatusEffect> entry = instance.getEffectType();
            active.add(entry);

            if (!USEFUL_BUFFS.contains(entry)) continue;

            int duration = instance.getDuration();
            if (duration <= 0) continue;

            float remaining = duration / 20f;
            if (remaining <= thresholdSec) {
                if (!warned.contains(entry)) {
                    warned.add(entry);
                    String name = Text.translatable(entry.value().getTranslationKey()).getString();
                    Interface.NotificationManager.postWarning("Баффни " + name + "! Осталось " + (int) remaining + "с!");
                }
            } else {
                warned.remove(entry);
            }
        }

        warned.removeIf(e2 -> !active.contains(e2));
    }

    @Override
    public void onDisable() {
        warned.clear();
        super.onDisable();
    }
}
