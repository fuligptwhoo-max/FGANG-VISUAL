package com.fgangvisuals.module.list.render;

import com.google.common.eventbus.Subscribe;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import com.fgangvisuals.event.list.EventHUD;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BooleanSetting;
import com.fgangvisuals.module.settings.ButtonSetting;
import com.fgangvisuals.module.settings.ColorSetting;
import com.fgangvisuals.module.settings.ModeSetting;
import com.fgangvisuals.module.settings.SliderSetting;
import com.fgangvisuals.module.list.render.hud.Interface;
import com.fgangvisuals.util.render.math.MathUtil;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@ModuleInformation(moduleName = "Custom Crosshair", moduleDesc = "Кастомный прицел", moduleCategory = ModuleCategory.RENDER)
public class CustomCrosshair extends Module {

    private static final Identifier CROSSHAIR_TEXTURE = Identifier.of("fgangvisuals", "custom_crosshair");
    private static final Path IMAGE_PATH = FabricLoader.getInstance().getConfigDir().resolve("fgangvisuals/crosshair.png");

    private final ModeSetting style = new ModeSetting("Стиль", "Cross", "Cross", "Dot", "Circle", "Image");
    private final SliderSetting size = new SliderSetting("Размер", 6, 1, 20, 0.5);
    private final SliderSetting width = new SliderSetting("Толщина", 1.5, 0.5, 5, 0.5);
    private final SliderSetting gap = new SliderSetting("Зазор", 2, 0, 10, 0.5);
    private final ColorSetting color = new ColorSetting("Цвет", 0xFFFFFFFF);
    private final BooleanSetting outline = new BooleanSetting("Обводка", true);
    private final ColorSetting outlineColor = new ColorSetting("Цвет обводки", 0xFF000000);
    private final SliderSetting outlineWidth = new SliderSetting("Толщина обводки", 0.5, 0.5, 3, 0.5);
    private final BooleanSetting dynamic = new BooleanSetting("Динамический", true);
    private final SliderSetting dynamicAmount = new SliderSetting("Динамика", 2, 0, 10, 0.5);
    private final ButtonSetting uploadImage = new ButtonSetting("Изображение", "Загрузить", this::uploadImage)
            .setVisible(() -> style.is("Image"));

    private NativeImageBackedTexture texture;
    private boolean textureLoaded = false;
    private long lastModified = -1;

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
            case "Image" -> drawImage(context, centerX, centerY);
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

    private void drawImage(DrawContext context, float cx, float cy) {
        if (!Files.exists(IMAGE_PATH)) {
            drawDot(context, cx, cy, width.getFloatValue(), color.getValue(), outlineColor.getValue(), outline.getValue() ? outlineWidth.getFloatValue() : 0);
            return;
        }

        try {
            long modified = Files.getLastModifiedTime(IMAGE_PATH).toMillis();
            if (!textureLoaded || modified != lastModified) {
                loadTexture(modified);
            }

            if (textureLoaded) {
                int drawSize = (int) (size.getFloatValue() * 2);
                int x = (int) (cx - drawSize / 2f);
                int y = (int) (cy - drawSize / 2f);
                context.drawTexture(RenderLayer::getGuiTextured, CROSSHAIR_TEXTURE, x, y, 0, 0, drawSize, drawSize, drawSize, drawSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
            textureLoaded = false;
        }
    }

    private void loadTexture(long modified) {
        if (texture != null) {
            texture.close();
            mc.getTextureManager().destroyTexture(CROSSHAIR_TEXTURE);
            texture = null;
        }

        try (InputStream stream = Files.newInputStream(IMAGE_PATH)) {
            NativeImage image = NativeImage.read(stream);
            texture = new NativeImageBackedTexture(image);
            mc.getTextureManager().registerTexture(CROSSHAIR_TEXTURE, texture);
            textureLoaded = true;
            lastModified = modified;
        } catch (Exception e) {
            e.printStackTrace();
            textureLoaded = false;
        }
    }

    private void uploadImage() {
        new Thread(() -> {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Images (PNG, JPG, GIF)", "png", "jpg", "jpeg", "gif"));
                chooser.setDialogTitle("Select crosshair image");
                int result = chooser.showOpenDialog(null);
                if (result != JFileChooser.APPROVE_OPTION) return;

                java.io.File selected = chooser.getSelectedFile();
                Files.createDirectories(IMAGE_PATH.getParent());
                Files.copy(selected.toPath(), IMAGE_PATH, StandardCopyOption.REPLACE_EXISTING);

                // Force reload on next render
                lastModified = -1;
                textureLoaded = false;

                Interface.NotificationManager.postWarning("Crosshair image uploaded: " + selected.getName());
            } catch (Throwable t) {
                t.printStackTrace();
                Interface.NotificationManager.postWarning("Failed to upload crosshair image");
            }
        }, "FGANG-Crosshair-Upload").start();
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
