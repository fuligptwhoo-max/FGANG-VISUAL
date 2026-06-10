package com.fgangvisuals.features.optimizations;

import com.fgangvisuals.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.event.tick.ClientTickEvent;

import java.util.HashMap;
import java.util.Map;

public class EntityCulling {
    private final Minecraft mc = Minecraft.getInstance();
    private final Map<Integer, CullData> cache = new HashMap<>();

    private static class CullData {
        boolean visible; long time;
        CullData(boolean v) { this.visible = v; this.time = System.currentTimeMillis(); }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (!Config.ENTITY_CULLING_ENABLED.get()) return;
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(e -> now - e.getValue().time > 5000);
    }

    @SubscribeEvent
    public void onPreRender(RenderLivingEvent.Pre<?, ?> event) {
        if (!Config.ENTITY_CULLING_ENABLED.get() || mc.player == null) return;
        Entity e = event.getEntity();
        if (e == mc.player) return;
        int id = e.getId();
        CullData d = cache.get(id);
        if (d != null && System.currentTimeMillis() - d.time < 150) {
            if (!d.visible) event.setCanceled(true);
            return;
        }
        boolean vis = isVisible(e);
        cache.put(id, new CullData(vis));
        if (!vis) event.setCanceled(true);
    }

    private boolean isVisible(Entity e) {
        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 pos = e.position();
        double maxDist = Config.ENTITY_CULLING_DISTANCE.get();
        if (cam.distanceTo(pos) > maxDist) return false;
        if (Config.SMART_CULLING.get()) {
            Vec3 look = mc.player.getLookAngle();
            Vec3 to = pos.subtract(cam).normalize();
            if (look.dot(to) < -0.3 && cam.distanceTo(pos) > 8) return false;
        }
        try {
            var field = mc.levelRenderer.getClass().getDeclaredField("cullingFrustum");
            field.setAccessible(true);
            Object frustum = field.get(mc.levelRenderer);
            if (frustum != null) {
                AABB box = e.getBoundingBox();
                return ((net.minecraft.client.renderer.culling.Frustum) frustum).isVisible(box);
            }
        } catch (Exception ignored) {}
        return true;
    }

    public boolean shouldRender(Entity e) {
        if (!Config.ENTITY_CULLING_ENABLED.get()) return true;
        CullData d = cache.get(e.getId());
        if (d != null && System.currentTimeMillis() - d.time < 150) return d.visible;
        boolean v = isVisible(e);
        cache.put(e.getId(), new CullData(v));
        return v;
    }

    public void clear() { cache.clear(); }
}
