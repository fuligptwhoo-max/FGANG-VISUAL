package com.fgangvisuals.module.list.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import com.fgangvisuals.event.list.EventAttack;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BooleanSetting;
import com.fgangvisuals.module.settings.ModeSetting;
import com.fgangvisuals.module.settings.SliderSetting;

import java.util.HashMap;
import java.util.Map;

@ModuleInformation(moduleName = "Hit Sounds", moduleDesc = "Звук при ударе по entity", moduleCategory = ModuleCategory.RENDER)
public class HitSounds extends Module {

    private static final Map<String, SoundEvent> SOUNDS = new HashMap<>();

    public static void registerSounds() {
        String[] names = {"get", "magic", "magic_pok", "pok", "run"};
        for (String name : names) {
            Identifier id = Identifier.of("mre", name);
            SoundEvent event = SoundEvent.of(id);
            Registry.register(Registries.SOUND_EVENT, id, event);
            SOUNDS.put(name, event);
        }
    }

    private final ModeSetting soundMode = new ModeSetting("Звук", "get",
            "get", "magic", "magic_pok", "pok", "run");

    private final SliderSetting volume = new SliderSetting("Громкость", 1.0, 0.0, 2.0, 0.1);

    private final BooleanSetting critOnly = new BooleanSetting("Только крит", false);

    @Subscribe
    private void onAttack(EventAttack e) {
        if (mc.player == null) return;
        if (!e.isCrit() && critOnly.getValue()) return;

        SoundEvent sound = SOUNDS.get(soundMode.getValue());
        if (sound == null) return;
        mc.getSoundManager().play(PositionedSoundInstance.master(sound, 1.0f, volume.getFloatValue()));
    }
}
