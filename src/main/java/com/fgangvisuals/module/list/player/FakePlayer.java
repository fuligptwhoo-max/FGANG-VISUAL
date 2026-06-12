package com.fgangvisuals.module.list.player;

import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import com.fgangvisuals.event.list.EventAttack;
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

    private float currentHealth;

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

        currentHealth = invincible.getValue() ? Float.MAX_VALUE : health.getFloatValue();
        fakePlayer.setHealth(currentHealth);

        fakePlayer.setPos(
                mc.player.getX() + offsetX.getValue(),
                mc.player.getY() + offsetY.getValue(),
                mc.player.getZ() + offsetZ.getValue()
        );
        mc.world.addEntity(fakePlayer);
    }

    @Subscribe
    private void onAttack(EventAttack event) {
        if (invincible.getValue()) return;
        if (fakePlayer == null || event.getEntity() != fakePlayer) return;

        float damage = 1.0f;
        ItemStack weapon = mc.player.getMainHandStack();
        if (weapon != null && !weapon.isEmpty()) {
            // Simple damage approximation; real calculation is much more complex
            damage = 2.0f + (float) (Math.random() * 2.0);
        }
        if (event.isCrit()) damage *= 1.5f;

        currentHealth -= damage;
        fakePlayer.setHealth(Math.max(currentHealth, 0));

        if (currentHealth <= 0) {
            fakePlayer.remove(Entity.RemovalReason.DISCARDED);
            mc.world.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
            fakePlayer = null;
        }
    }

    @Override
    public void onDisable() {
        if (mc.world != null && fakePlayer != null) {
            mc.world.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
            fakePlayer = null;
        }
    }
}
