package com.fgangvisuals.features.optimizations;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class BlockEntityOptimizer {
    private final Minecraft mc = Minecraft.getInstance();
    private int tick = 0;

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.BLOCK_ENTITY_OPTIMIZER.get()) return;
        tick++;
    }

    public boolean shouldRender(BlockEntity be) {
        if (!Config.BLOCK_ENTITY_OPTIMIZER.get()) return true;
        if (mc.player == null) return true;
        BlockPos pos = be.getBlockPos();
        double max = Config.BLOCK_ENTITY_DISTANCE.get();
        return Math.sqrt(pos.distSqr(mc.player.blockPosition())) <= max;
    }
}
