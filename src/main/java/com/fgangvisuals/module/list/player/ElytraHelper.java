package com.fgangvisuals.module.list.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Formatting;
import com.fgangvisuals.event.list.EventKeyInput;
import com.fgangvisuals.event.list.EventPlayerUpdate;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BindSetting;
import com.fgangvisuals.util.chat.ChatUtil;
import com.fgangvisuals.util.player.other.InventoryUtil;

@ModuleInformation(moduleName = "Elytra Helper", moduleCategory = ModuleCategory.PLAYER)
public class ElytraHelper extends Module {
    private final BindSetting swapKey = new BindSetting("Кнопка свапа", -1);

    private boolean swapped;

    @Subscribe
    private void onKey(EventKeyInput e) {
        if (e.getAction() == 0) return;
        if (e.getKey() == swapKey.getValue()) swapped = true;
    }

    @Subscribe
    private void onPlayerUpdate(EventPlayerUpdate e) {
        if (!swapped) return;
        swapped = false;
        polarSwap(mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA);
    }

    private void polarSwap(boolean chestplate) {
        var slot = chestplate ? InventoryUtil.findBestChestplateSlot() : InventoryUtil.findBestElytraSlot();

        if (slot == -1) {
            var info = "Элитра" + Formatting.GRAY + " не найдена в инвентаре";
            if (chestplate) info = "Нагрудник" + Formatting.GRAY + " не найден в инвентаре";
            ChatUtil.send(info);
            return;
        }

        if (slot >= 0 && slot <= 8) {
            InventoryUtil.swapWithBypassPolar(() -> {
                mc.interactionManager.clickSlot(0, 6, slot, SlotActionType.SWAP, mc.player);
            });
        } else if (slot >= 8 && slot <= 45) {
            InventoryUtil.swapWithBypassPolar(() -> {
                mc.interactionManager.clickSlot(0, slot, 8, SlotActionType.SWAP, mc.player);
                mc.interactionManager.clickSlot(0, 6, 8, SlotActionType.SWAP, mc.player);
                mc.interactionManager.clickSlot(0, slot, 8, SlotActionType.SWAP, mc.player);
            });
        }
    }
}
