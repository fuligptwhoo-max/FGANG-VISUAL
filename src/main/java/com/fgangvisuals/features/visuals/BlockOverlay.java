package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class BlockOverlay {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRenderLevel(RenderLevelStageEvent event) {
        if (!Config.BLOCK_OVERLAY_ENABLED.get()) return;
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;
        if (mc.player == null || mc.level == null) return;
        if (!(mc.hitResult instanceof BlockHitResult bhr)) return;

        BlockPos pos = bhr.getBlockPos();
        BlockState state = mc.level.getBlockState(pos);
        if (state.isAir()) return;

        VoxelShape shape = state.getShape(mc.level, pos);
        if (shape.isEmpty()) return;

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        double cx = cam.x, cy = cam.y, cz = cam.z;

        int col = Config.BLOCK_OVERLAY_COLOR.get();
        float r = ((col >> 16) & 0xFF) / 255f;
        float g = ((col >> 8) & 0xFF) / 255f;
        float b = (col & 0xFF) / 255f;
        float a = Config.BLOCK_OVERLAY_ALPHA.get().floatValue();

        PoseStack pose = event.getPoseStack();
        pose.pushPose();
        pose.translate(pos.getX() - cx, pos.getY() - cy, pos.getZ() - cz);

        if (Config.BLOCK_OVERLAY_FILL.get()) {
            VertexConsumer vc = mc.renderBuffers().bufferSource().getBuffer(RenderType.debugFilledBox());
            shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
                LevelRenderer.addChainedFilledBoxVertices(pose, vc, x1, y1, z1, x2, y2, z2, r, g, b, a);
            });
        }
        if (Config.BLOCK_OVERLAY_OUTLINE.get()) {
            VertexConsumer vc = mc.renderBuffers().bufferSource().getBuffer(RenderType.lines());
            shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
                vc.addVertex(pose.last().pose(), (float)x1, (float)y1, (float)z1).setColor(r, g, b, 1f).setNormal(0,1,0);
                vc.addVertex(pose.last().pose(), (float)x2, (float)y2, (float)z2).setColor(r, g, b, 1f).setNormal(0,1,0);
            });
        }
        pose.popPose();
    }
}
