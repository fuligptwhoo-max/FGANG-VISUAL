package com.fgangvisuals.module.list.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.fgangvisuals.event.list.EventKeyInput;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.ItemListSetting;
import com.fgangvisuals.util.chat.ChatUtil;

@ModuleInformation(moduleName = "Server Helper", moduleDesc = "Быстрое использование предметов", moduleCategory = ModuleCategory.PLAYER)
public class ServerHelper extends Module {

    public final ItemListSetting items = new ItemListSetting("Предметы");

    public boolean pickingItem = false;

    @Subscribe
    private void onKey(EventKeyInput event) {
        if (mc.player == null) return;
        if (mc.currentScreen != null) return;
        if (event.getAction() != 1) return;

        for (int i = 0; i < items.size(); i++) {
            ServerHelperItem item = items.get(i);
            if (item.key == -1) continue;
            if (item.key == event.getKey()) {
                selectItem(item.itemId);
                return;
            }
        }
    }

    @Subscribe
    private void onSlotClick(com.fgangvisuals.event.list.EventSlotClick event) {
        if (!pickingItem) return;
        if (event.getButton() != 0) return;
        if (!(mc.currentScreen instanceof net.minecraft.client.gui.screen.ingame.InventoryScreen)) return;

        var slot = event.getSlot();
        if (slot != null && slot.hasStack()) {
            String id = Registries.ITEM.getId(slot.getStack().getItem()).toString();
            items.add(new ServerHelperItem(id, -1));
            pickingItem = false;
            ChatUtil.send("§aПредмет §e" + id + " §aдобавлен!");
            mc.player.closeHandledScreen();
        }
    }

    private void selectItem(String itemId) {
        Identifier id = Identifier.tryParse(itemId);
        if (id == null) return;

        int slot = -1;
        for (int i = 0; i < 9; i++) {
            if (Registries.ITEM.getId(mc.player.getInventory().getStack(i).getItem()).equals(id)) {
                slot = i;
                break;
            }
        }

        if (slot == -1) {
            ChatUtil.send("§cВозьмите расходник в хотбар");
            return;
        }

        mc.player.getInventory().selectedSlot = slot;
        mc.interactionManager.syncSelectedSlot();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        pickingItem = false;
    }
}
