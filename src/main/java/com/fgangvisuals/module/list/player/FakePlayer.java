package com.fgangvisuals.module.list.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BooleanSetting;
import com.fgangvisuals.module.settings.ModeSetting;
import com.fgangvisuals.module.settings.SliderSetting;

import java.util.UUID;

@ModuleInformation(moduleName = "Fake Player", moduleCategory = ModuleCategory.MISC)
public class FakePlayer extends Module {
    private OtherClientPlayerEntity fakePlayer;

    private final ModeSetting nameMode = new ModeSetting("Имя", "Own", "Own", "Random");
    private final BooleanSetting invincible = new BooleanSetting("Бессмертие", true);
    private final SliderSetting health = new SliderSetting("Здоровье", 20, 1, 20, 0.5);
    private final BooleanSetting copyInventory = new BooleanSetting("Копировать инвентарь", true);
    private final BooleanSetting copyArmor = new BooleanSetting("Копировать броню", true);
    private final SliderSetting offsetX = new SliderSetting("Смещение X", 0, -5, 5, 0.1);
    private final SliderSetting offsetY = new SliderSetting("Смещение Y", 0, -5, 5, 0.1);
    private final SliderSetting offsetZ = new SliderSetting("Смещение Z", 0, -5, 5, 0.1);

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) return;

        String name = nameMode.is("Own") ? mc.player.getName().getString() : "Fake" + (int) (Math.random() * 10000);
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        fakePlayer = new OtherClientPlayerEntity(mc.world, profile);

        fakePlayer.copyFrom(mc.player);

        if (!copyInventory.getValue()) {
            for (int i = 0; i < fakePlayer.getInventory().main.size(); i++) {
                fakePlayer.getInventory().main.set(i, ItemStack.EMPTY);
            }
            fakePlayer.getInventory().offHand.set(0, ItemStack.EMPTY);
        }

        if (!copyArmor.getValue()) {
            fakePlayer.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
            fakePlayer.equipStack(EquipmentSlot.CHEST, ItemStack.EMPTY);
            fakePlayer.equipStack(EquipmentSlot.LEGS, ItemStack.EMPTY);
            fakePlayer.equipStack(EquipmentSlot.FEET, ItemStack.EMPTY);
        }

        if (!invincible.getValue()) {
            fakePlayer.setHealth(health.getFloatValue());
        } else {
            fakePlayer.setHealth(Float.MAX_VALUE);
        }

        fakePlayer.setPos(
                mc.player.getX() + offsetX.getValue(),
                mc.player.getY() + offsetY.getValue(),
                mc.player.getZ() + offsetZ.getValue()
        );
        mc.world.addEntity(fakePlayer);
    }

    @Override
    public void onDisable() {
        if (mc.world != null && fakePlayer != null) {
            mc.world.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
            fakePlayer = null;
        }
    }
}
