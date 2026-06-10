package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

public class HealthBarESP {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        if (!Config.HEALTH_BAR_ESP_ENABLED.get()) return;
        LivingEntity e = event.getEntity();
        if (e == mc.player) return;
        if (e.distanceTo(mc.player) > 32) return;

        float health = e.getHealth();
        float max = e.getMaxHealth();
        float pct = health / max;

        PoseStack pose = event.getPoseStack();
        pose.pushPose();
        Vec3 pos = e.position();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        pose.translate(pos.x - cam.x, pos.y - cam.y + e.getBbHeight() + Config.HEALTH_BAR_ESP_HEIGHT.get(), pos.z - cam.z);
        pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        pose.scale(-0.025f, -0.025f, 0.025f);

        float w = Config.HEALTH_BAR_ESP_WIDTH.get().floatValue() * 20;
        float h = 2;
        float x = -w / 2;
        VertexConsumer vc = mc.renderBuffers().bufferSource().getBuffer(RenderType.debugQuads());
        vc.addVertex(pose.last().pose(), x, 0, 0).setColor(0.2f, 0.2f, 0.2f, 1f);
        vc.addVertex(pose.last().pose(), x, h, 0).setColor(0.2f, 0.2f, 0.2f, 1f);
        vc.addVertex(pose.last().pose(), x + w, h, 0).setColor(0.2f, 0.2f, 0.2f, 1f);
        vc.addVertex(pose.last().pose(), x + w, 0, 0).setColor(0.2f, 0.2f, 0.2f, 1f);

        int col = pct > 0.6f ? 0x33FF33 : pct > 0.3f ? 0xFFFF33 : 0xFF3333;
        float r = ((col >> 16) & 0xFF) / 255f;
        float g = ((col >> 8) & 0xFF) / 255f;
        float b = (col & 0xFF) / 255f;
        float fw = w * pct;
        vc.addVertex(pose.last().pose(), x, 0, 0).setColor(r, g, b, 1f);
        vc.addVertex(pose.last().pose(), x, h, 0).setColor(r, g, b, 1f);
        vc.addVertex(pose.last().pose(), x + fw, h, 0).setColor(r, g, b, 1f);
        vc.addVertex(pose.last().pose(), x + fw, 0, 0).setColor(r, g, b, 1f);

        pose.popPose();
    }
}
