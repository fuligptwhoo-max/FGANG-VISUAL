package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class DynamicLights {
    private final Minecraft mc = Minecraft.getInstance();
    private BlockPos lastPos = null;

    @SubscribeEvent
    public void onTick(PlayerTickEvent.Post event) {
        if (!Config.DYNAMIC_LIGHTS_ENABLED.get() || mc.player == null || mc.level == null) return;
        Player p = mc.player;
        int lvl = getLight(p.getMainHandItem());
        if (lvl == 0) lvl = getLight(p.getOffhandItem());

        BlockPos pos = p.blockPosition();
        if (lvl > 0) {
            if (!pos.equals(lastPos)) {
                mc.level.getLightEngine().checkBlock(pos);
                if (lastPos != null) mc.level.getLightEngine().checkBlock(lastPos);
                lastPos = pos;
            }
        } else if (lastPos != null) {
            mc.level.getLightEngine().checkBlock(lastPos);
            lastPos = null;
        }
    }

    private int getLight(ItemStack s) {
        if (s.isEmpty()) return 0;
        if (s.is(Items.TORCH)) return 14;
        if (s.is(Items.LANTERN) || s.is(Items.SOUL_LANTERN)) return 15;
        if (s.is(Items.GLOWSTONE)) return 15;
        if (s.is(Items.REDSTONE_TORCH)) return 7;
        if (s.is(Items.LAVA_BUCKET)) return 15;
        if (s.is(Items.SHROOMLIGHT) || s.is(Items.JACK_O_LANTERN) || s.is(Items.CAMPFIRE) || s.is(Items.SOUL_CAMPFIRE)) return 15;
        if (s.is(Items.BLAZE_ROD)) return 10;
        return 0;
    }
}
