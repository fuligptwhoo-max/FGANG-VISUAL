package com.fgangvisuals.module.list.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BooleanSetting;
import com.fgangvisuals.module.settings.ColorSetting;
import com.fgangvisuals.module.settings.SliderSetting;

@ModuleInformation(moduleName = "Custom Hitbox", moduleDesc = "Кастомные хитбоксы сущностей", moduleCategory = ModuleCategory.RENDER)
public class CustomHitbox extends Module {

    public final ColorSetting hitboxColor = new ColorSetting("Цвет хитбокса", 0xFFFF0000);
    public final BooleanSetting fill = new BooleanSetting("Заливка", false);
    public final SliderSetting fillOpacity = new SliderSetting("Прозрачность", 0.3f, 0.0f, 1.0f, 0.05f);

    private boolean registered = false;
    private final WorldRenderEvents.Last listener = context -> {
        onRenderWorldLast(context.matrixStack(), context.camera(), context.tickCounter().getTickDelta(true));
    };

    @Override
    public void onEnable() {
        if (!registered) {
            WorldRenderEvents.LAST.register(listener);
            registered = true;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void onRenderWorldLast(MatrixStack matrices, Camera camera, float tickDelta) {
        if (!isEnabled() || mc.world == null || mc.player == null) return;

        Vec3d camPos = camera.getPos();
        int color = hitboxColor.getValue();

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player) continue;

            boolean isPlayer = entity instanceof PlayerEntity;
            boolean isItem = entity instanceof ItemEntity;
            if (!isPlayer && !isItem) continue;

            if (isPlayer) {
                LivingEntity living = (LivingEntity) entity;
                if (living.isInvisible() && living instanceof PlayerEntity player && !hasArmor(player)) continue;

                double x = interpolate(living.getX(), living.lastRenderX, tickDelta);
                double y = interpolate(living.getY(), living.lastRenderY, tickDelta);
                double z = interpolate(living.getZ(), living.lastRenderZ, tickDelta);

                double hw = living.getWidth() / 2.0;
                double h = living.getHeight();

                matrices.push();
                drawBox(matrices, x - camPos.x, y - camPos.y, z - camPos.z, hw, h, color);
                matrices.pop();

            } else if (isItem) {
                double x = interpolate(entity.getX(), entity.lastRenderX, tickDelta);
                double y = interpolate(entity.getY(), entity.lastRenderY, tickDelta);
                double z = interpolate(entity.getZ(), entity.lastRenderZ, tickDelta);

                var box = entity.getBoundingBox();
                double hw = (box.maxX - box.minX) / 2.0;
                double hh = box.maxY - box.minY;
                double hz = (box.maxZ - box.minZ) / 2.0;

                matrices.push();
                drawBox(matrices, x - camPos.x, y - camPos.y, z - camPos.z, hw, hh, hz, color);
                matrices.pop();
            }
        }
    }

    private boolean hasArmor(PlayerEntity player) {
        return !player.getEquippedStack(EquipmentSlot.HEAD).isEmpty()
            || !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()
            || !player.getEquippedStack(EquipmentSlot.LEGS).isEmpty()
            || !player.getEquippedStack(EquipmentSlot.FEET).isEmpty();
    }

    private void drawBox(MatrixStack matrices, double cx, double cy, double cz, double hw, double h, int color) {
        drawBox(matrices, cx, cy, cz, hw, h, hw, color);
    }

    private void drawBox(MatrixStack matrices, double cx, double cy, double cz, double hx, double h, double hz, int color) {
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        Matrix4f m = matrices.peek().getPositionMatrix();

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;
        if (a == 0) a = 1f;

        float minX = (float) (cx - hx);
        float minY = (float) cy;
        float minZ = (float) (cz - hz);
        float maxX = (float) (cx + hx);
        float maxY = (float) (cy + h);
        float maxZ = (float) (cz + hz);

        // Fill
        if (fill.getValue()) {
            float fa = fillOpacity.getFloatValue();
            RenderSystem.disableCull();
            BufferBuilder buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);

            // 6 faces, 2 triangles each
            // bottom
            buf.vertex(m, minX, minY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, minZ).color(r, g, b, fa);
            buf.vertex(m, minX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, minX, minY, maxZ).color(r, g, b, fa);
            // top
            buf.vertex(m, minX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, minZ).color(r, g, b, fa);
            // front (-Z)
            buf.vertex(m, minX, minY, minZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, minZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, minZ).color(r, g, b, fa);
            // back (+Z)
            buf.vertex(m, minX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, maxZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, maxZ).color(r, g, b, fa);
            // left (-X)
            buf.vertex(m, minX, minY, minZ).color(r, g, b, fa);
            buf.vertex(m, minX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, minX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, maxZ).color(r, g, b, fa);
            buf.vertex(m, minX, maxY, minZ).color(r, g, b, fa);
            // right (+X)
            buf.vertex(m, maxX, minY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, minY, maxZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, minZ).color(r, g, b, fa);
            buf.vertex(m, maxX, maxY, maxZ).color(r, g, b, fa);

            BufferRenderer.drawWithGlobalProgram(buf.end());
            RenderSystem.enableCull();
        }

        // Outline
        BufferBuilder buf = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        // bottom
        buf.vertex(m, minX, minY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, minY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, minY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, minY, maxZ).color(r, g, b, a);
        buf.vertex(m, maxX, minY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, minY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, minY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, minY, minZ).color(r, g, b, a);
        // top
        buf.vertex(m, minX, maxY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, maxY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, maxY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, maxY, maxZ).color(r, g, b, a);
        buf.vertex(m, maxX, maxY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, maxY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, maxY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, maxY, minZ).color(r, g, b, a);
        // vertical
        buf.vertex(m, minX, minY, minZ).color(r, g, b, a);
        buf.vertex(m, minX, maxY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, minY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, maxY, minZ).color(r, g, b, a);
        buf.vertex(m, maxX, minY, maxZ).color(r, g, b, a);
        buf.vertex(m, maxX, maxY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, minY, maxZ).color(r, g, b, a);
        buf.vertex(m, minX, maxY, maxZ).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(buf.end());
        RenderSystem.disableBlend();
    }

    private double interpolate(double current, double previous, double tickDelta) {
        return previous + (current - previous) * tickDelta;
    }
}
