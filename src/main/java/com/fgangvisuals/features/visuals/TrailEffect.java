package com.fgangvisuals.features.visuals;

import com.fgangvisuals.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class TrailEffect {
    private final Minecraft mc = Minecraft.getInstance();
    private final List<TrailPoint> points = new ArrayList<>();

    private static class TrailPoint {
        double x, y, z;
        long time;
        TrailPoint(double x, double y, double z) { this.x=x; this.y=y; this.z=z; this.time=System.currentTimeMillis(); }
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent.Post event) {
        if (!Config.TRAIL_ENABLED.get() || mc.player == null) return;
        if (Config.TRAIL_ONLY_SELF.get() && event.getEntity() != mc.player) return;

        Vec3 motion = mc.player.getDeltaMovement();
        if (motion.lengthSqr() > 0.001) {
            double x = mc.player.getX();
            double y = mc.player.getY() + mc.player.getBbHeight() * 0.5;
            double z = mc.player.getZ();
            if (points.isEmpty() || dist(points.get(points.size()-1), x, y, z) > 0.3) {
                points.add(new TrailPoint(x, y, z));
            }
        }

        long maxAge = (long) (Config.TRAIL_LENGTH.get() * 1000);
        points.removeIf(p -> System.currentTimeMillis() - p.time > maxAge);
        while (points.size() > Config.TRAIL_MAX_POINTS.get()) points.remove(0);
    }

    public void render(com.mojang.blaze3d.vertex.PoseStack poseStack, Matrix4f projection, Matrix4f modelView) {
        if (!Config.TRAIL_ENABLED.get() || points.size() < 2) return;
        int col = Config.TRAIL_COLOR.get();
        float r = ((col >> 16) & 0xFF) / 255f;
        float g = ((col >> 8) & 0xFF) / 255f;
        float b = (col & 0xFF) / 255f;
        long maxAge = (long) (Config.TRAIL_LENGTH.get() * 1000);
        float width = Config.TRAIL_WIDTH.get().floatValue();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator t = Tesselator.getInstance();
        BufferBuilder builder = t.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f m = poseStack.last().pose();
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

        for (int i = 0; i < points.size() - 1; i++) {
            TrailPoint a = points.get(i);
            TrailPoint b2 = points.get(i+1);
            float age = System.currentTimeMillis() - a.time;
            float alpha = Math.max(0, 1f - (age / maxAge));
            float w = width * alpha;

            double ax = a.x - cam.x, ay = a.y - cam.y, az = a.z - cam.z;
            double bx2 = b2.x - cam.x, by2 = b2.y - cam.y, bz2 = b2.z - cam.z;

            builder.addVertex(m, (float)ax, (float)(ay-w), (float)az).setColor(r, g, b, alpha * 0.8f);
            builder.addVertex(m, (float)ax, (float)(ay+w), (float)az).setColor(r, g, b, alpha * 0.8f);
            builder.addVertex(m, (float)bx2, (float)(by2+w), (float)bz2).setColor(r, g, b, alpha * 0.8f);
            builder.addVertex(m, (float)bx2, (float)(by2-w), (float)bz2).setColor(r, g, b, alpha * 0.8f);
        }
        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private double dist(TrailPoint p, double x, double y, double z) {
        double dx = p.x - x, dy = p.y - y, dz = p.z - z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public void clear() { points.clear(); }
}
