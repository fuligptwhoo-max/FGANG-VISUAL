package com.fgangvisuals.module.list.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import com.fgangvisuals.event.list.EventKeyInput;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BindSetting;
import com.fgangvisuals.util.chat.ChatUtil;

@ModuleInformation(moduleName = "Click Pearl", moduleCategory = ModuleCategory.PLAYER)
public class ClickPearl extends Module {
    private final BindSetting key = new BindSetting("Клавиша", -98);

    @Subscribe
    private void onKey(EventKeyInput e) {
        if (e.getAction() == 0) return;
        if (e.getKey() != key.getValue()) return;
        if (mc.player == null) return;

        int slot = -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isOf(Items.ENDER_PEARL)) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            ChatUtil.send("§cВозьмите перл в хотбар");
            return;
        }

        mc.player.getInventory().selectedSlot = slot;
        mc.interactionManager.syncSelectedSlot();
    }
}
