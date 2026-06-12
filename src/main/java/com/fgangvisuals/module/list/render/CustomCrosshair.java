package com.fgangvisuals.module.list.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import com.fgangvisuals.event.list.EventHUD;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BooleanSetting;
import com.fgangvisuals.module.settings.ColorSetting;
import com.fgangvisuals.module.settings.ModeSetting;
import com.fgangvisuals.module.settings.SliderSetting;
import com.fgangvisuals.util.render.math.MathUtil;

@ModuleInformation(moduleName = "Custom Crosshair", moduleDesc = "Кастомный прицел", moduleCategory = ModuleCategory.RENDER)
public class CustomCrosshair extends Module {

    private final ModeSetting style = new ModeSetting("Стиль", "Cross", "Cross", "Dot", "Circle");
    private final SliderSetting size = new SliderSetting("Размер", 6, 1, 20, 0.5);
    private final SliderSetting width = new SliderSetting("Толщина", 1.5, 0.5, 5, 0.5);
    private final SliderSetting gap = new SliderSetting("Зазор", 2, 0, 10, 0.5);
    private final ColorSetting color = new ColorSetting("Цвет", 0xFFFFFFFF);
    private final BooleanSetting outline = new BooleanSetting("Обводка", true);
    private final ColorSetting outlineColor = new ColorSetting("Цвет обводки", 0xFF000000);
    private final SliderSetting outlineWidth = new SliderSetting("Толщина обводки", 0.5, 0.5, 3, 0.5);
    private final BooleanSetting dynamic = new BooleanSetting("Динамический", true);
    private final SliderSetting dynamicAmount = new SliderSetting("Динамика", 2, 0, 10, 0.5);

    @Subscribe
    public void onRender(EventHUD event) {
        DrawContext context = event.getDrawContext();
        renderCrosshair(context);
    }

    private void renderCrosshair(DrawContext context) {
        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();
        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        float dynamicGap = dynamic.getValue() ? calculateDynamicGap() : 0;
        float currentGap = gap.getFloatValue() + dynamicGap;
        float currentSize = size.getFloatValue();
        float currentWidth = width.getFloatValue();

        int fill = color.getValue();
        int stroke = outlineColor.getValue();
        float strokeWidth = outline.getValue() ? outlineWidth.getFloatValue() : 0;

        switch (style.getValue()) {
            case "Cross" -> drawCross(context, centerX, centerY, currentSize, currentGap, currentWidth, fill, stroke, strokeWidth);
            case "Dot" -> drawDot(context, centerX, centerY, currentWidth, fill, stroke, strokeWidth);
            case "Circle" -> drawCircle(context, centerX, centerY, currentSize, currentWidth, fill, stroke, strokeWidth);
        }
    }

    private void drawCross(DrawContext context, float cx, float cy, float size, float gap, float thickness, int fill, int stroke, float strokeWidth) {
        // Top
        drawRect(context, cx - thickness / 2, cy - gap - size, thickness, size, fill, stroke, strokeWidth);
        // Bottom
        drawRect(context, cx - thickness / 2, cy + gap, thickness, size, fill, stroke, strokeWidth);
        // Left
        drawRect(context, cx - gap - size, cy - thickness / 2, size, thickness, fill, stroke, strokeWidth);
        // Right
        drawRect(context, cx + gap, cy - thickness / 2, size, thickness, fill, stroke, strokeWidth);
    }

    private void drawDot(DrawContext context, float cx, float cy, float size, int fill, int stroke, float strokeWidth) {
        drawRect(context, cx - size / 2, cy - size / 2, size, size, fill, stroke, strokeWidth);
    }

    private void drawCircle(DrawContext context, float cx, float cy, float radius, float thickness, int fill, int stroke, float strokeWidth) {
        // Simplified as a hollow square ring for now; replace with circle renderer if available
        drawRect(context, cx - radius, cy - thickness / 2, radius * 2, thickness, fill, stroke, strokeWidth);
        drawRect(context, cx - thickness / 2, cy - radius, thickness, radius * 2, fill, stroke, strokeWidth);
    }

    private void drawRect(DrawContext context, float x, float y, float w, float h, int fill, int stroke, float strokeWidth) {
        int xi = (int) MathUtil.round(x);
        int yi = (int) MathUtil.round(y);
        int wi = (int) MathUtil.round(w);
        int hi = (int) MathUtil.round(h);

        if (strokeWidth > 0) {
            int sw = (int) MathUtil.round(strokeWidth);
            context.fill(xi - sw, yi - sw, xi + wi + sw, yi + hi + sw, stroke);
        }
        context.fill(xi, yi, xi + wi, yi + hi, fill);
    }

    private float calculateDynamicGap() {
        if (mc.player == null) return 0;
        float velocity = (float) Math.sqrt(mc.player.getVelocity().x * mc.player.getVelocity().x + mc.player.getVelocity().z * mc.player.getVelocity().z);
        return Math.min(velocity * dynamicAmount.getFloatValue() * 10, dynamicAmount.getFloatValue() * 3);
    }
}
