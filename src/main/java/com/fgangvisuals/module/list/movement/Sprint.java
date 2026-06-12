package com.fgangvisuals.module.list.movement;

import com.google.common.eventbus.Subscribe;
import com.fgangvisuals.event.list.EventTick;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;

@ModuleInformation(moduleName = "Sprint", moduleDesc = "Автоматический спринт", moduleCategory = ModuleCategory.MOVEMENT)
public class Sprint extends Module {
    @Subscribe
    public void onUpdate(EventTick event) {
        if (mc.player == null) return;
        mc.options.sprintKey.setPressed(false);
        mc.player.setSprinting((!mc.player.isTouchingWater() || mc.player.isSubmergedInWater()) && !mc.player.isGliding() && mc.player.isWalking() && mc.player.canSprint() && !mc.player.isUsingItem() && !mc.player.isBlind() && (!mc.player.hasVehicle() || (mc.player.getVehicle().canSprintAsVehicle() && mc.player.getVehicle().isLogicalSideForUpdatingMovement()) && !mc.player.isGliding() && (!mc.player.shouldSlowDown() || mc.player.isSubmergedInWater())) && mc.player.input.hasForwardMovement() && (!mc.player.horizontalCollision && !mc.player.collidedSoftly));
    }
}