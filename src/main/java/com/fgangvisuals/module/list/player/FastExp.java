package com.fgangvisuals.module.list.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Items;
import com.fgangvisuals.event.list.EventPlayerUpdate;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;

@ModuleInformation(moduleName = "Fast Exp", moduleCategory = ModuleCategory.PLAYER)
public class FastExp extends Module {

    @Subscribe
    private void onPlayerUpdate(EventPlayerUpdate e) {
        if (!(mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getOffHandStack().getItem() == Items.EXPERIENCE_BOTTLE)) return;

        mc.itemUseCooldown = 0;
    }
}