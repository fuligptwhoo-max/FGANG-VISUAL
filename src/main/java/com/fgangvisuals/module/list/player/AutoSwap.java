package com.fgangvisuals.module.list.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;
import com.fgangvisuals.event.list.EventPlayerUpdate;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BindSetting;
import com.fgangvisuals.module.settings.ModeSetting;

@ModuleInformation(moduleName = "Auto Swap", moduleDesc = "Циклическая смена предмета в оффхенде", moduleCategory = ModuleCategory.PLAYER)
public class AutoSwap extends Module {

    private final ModeSetting type1 = new ModeSetting("Тип 1", "Тотем", "Тотем", "Сфера");
    private final ModeSetting type2 = new ModeSetting("Тип 2", "Сфера", "Тотем", "Сфера");
    private final BindSetting swapKey = new BindSetting("Кнопка", GLFW.GLFW_KEY_F);

    private int currentSlot = 0;
    private boolean wasKeyDown = false;
    private boolean swapping = false;
    private int swapTicks = 0;
    private int targetSlot = -1;
    private net.minecraft.item.Item targetItem = null;

    @Subscribe
    private void onUpdate(EventPlayerUpdate e) {
        if (mc.player == null) return;

        int k = swapKey.getValue();
        if (k >= 0) {
            boolean isDown = GLFW.glfwGetKey(mc.getWindow().getHandle(), k) == GLFW.GLFW_PRESS;
            if (isDown && !wasKeyDown) {
                startSwap();
            }
            wasKeyDown = isDown;
        }

        if (swapping) tickSwap();
    }

    private void startSwap() {
        String type = currentSlot == 0 ? type1.getValue() : type2.getValue();
        currentSlot = 1 - currentSlot;

        targetItem = type.equals("Тотем") ? Items.TOTEM_OF_UNDYING : Items.PLAYER_HEAD;

        // Mixed mode (Тотем/Сфера): skip if offhand already has the target
        if (!type1.getValue().equals(type2.getValue())) {
            if (mc.player.getOffHandStack().isOf(targetItem)) {
                targetItem = null;
                return;
            }
        }

        targetSlot = findItem(targetItem);

        // Same-type mode with item in offhand → move it back to inventory
        if (targetSlot == -1 && type1.getValue().equals(type2.getValue()) && mc.player.getOffHandStack().isOf(targetItem)) {
            for (int i = 0; i < 36; i++) {
                if (mc.player.getInventory().getStack(i).isEmpty()) {
                    targetSlot = i;
                    break;
                }
            }
            if (targetSlot == -1) {
                targetItem = null;
                return;
            }
            mc.setScreen(new InventoryScreen(mc.player));
            swapping = true;
            swapTicks = 0;
            return;
        }

        if (targetSlot == -1) {
            targetItem = null;
            return;
        }

        mc.setScreen(new InventoryScreen(mc.player));
        swapping = true;
        swapTicks = 0;
    }

    private void tickSwap() {
        swapTicks++;

        if (swapTicks == 2) {
            int syncId = mc.player.currentScreenHandler.syncId;
            if (targetSlot < 9) {
                mc.interactionManager.clickSlot(syncId, 45, targetSlot, SlotActionType.SWAP, mc.player);
            } else {
                int tempSlot = findEmptyOrLastHotbar();
                mc.interactionManager.clickSlot(syncId, targetSlot, tempSlot, SlotActionType.SWAP, mc.player);
                mc.interactionManager.clickSlot(syncId, 45, tempSlot, SlotActionType.SWAP, mc.player);
                mc.interactionManager.clickSlot(syncId, targetSlot, tempSlot, SlotActionType.SWAP, mc.player);
            }
        }

        if (swapTicks >= 4) {
            mc.player.closeHandledScreen();
            swapping = false;
            targetSlot = -1;
            targetItem = null;
        }
    }

    private int findItem(net.minecraft.item.Item item) {
        for (int i = 9; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).isOf(item)) return i;
        }
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isOf(item)) return i;
        }
        return -1;
    }

    private int findEmptyOrLastHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) return i;
        }
        return 8;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        wasKeyDown = false;
        swapping = false;
        targetSlot = -1;
        targetItem = null;
    }
}
