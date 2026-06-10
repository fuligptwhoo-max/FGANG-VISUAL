package com.fgangvisuals.crosshair;

import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.utils.RenderUtils;
import com.fgangvisuals.config.Config;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CrosshairManager {
    private final Minecraft mc = Minecraft.getInstance();
    private final Path crosshairDir;
    private ResourceLocation customTexture = null;
    private DynamicTexture dynamicTexture = null;
    private String loadedFileName = "";

    public CrosshairManager() {
        crosshairDir = new File("config/" + FGANGVisuals.MOD_ID + "/crosshairs").toPath();
        try {
            Files.createDirectories(crosshairDir);
        } catch (IOException e) {
            FGANGVisuals.LOGGER.error("Failed to create crosshair directory", e);
        }
        loadDefault();
    }

    public void loadDefault() {
        File[] files = crosshairDir.toFile().listFiles((d, n) -> n.endsWith(".png") || n.endsWith(".jpg"));
        if (files != null && files.length > 0) {
            loadImage(files[0]);
        }
    }

    public void browseForImage() {
        new Thread(() -> {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("Images", "png", "jpg", "jpeg", "gif"));
                chooser.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selected = chooser.getSelectedFile();
                    File dest = new File(crosshairDir.toFile(), selected.getName());
                    Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Minecraft.getInstance().execute(() -> loadImage(dest));
                }
            } catch (Exception e) {
                FGANGVisuals.LOGGER.error("Failed to browse for crosshair image", e);
            }
        }).start();
    }

    public void loadImage(File file) {
        try {
            if (dynamicTexture != null) {
                dynamicTexture.close();
            }
            NativeImage image = NativeImage.read(new FileInputStream(file));
            dynamicTexture = new DynamicTexture(image);
            customTexture = ResourceLocation.fromNamespaceAndPath(FGANGVisuals.MOD_ID, "crosshair/" + file.getName().toLowerCase().replaceAll("[^a-z0-9_.-]", ""));
            mc.getTextureManager().register(customTexture, dynamicTexture);
            loadedFileName = file.getName();
            FGANGVisuals.LOGGER.info("Loaded crosshair: " + file.getName());
        } catch (Exception e) {
            FGANGVisuals.LOGGER.error("Failed to load crosshair image", e);
            customTexture = null;
        }
    }

    public void render(GuiGraphics g) {
        if (!Config.CROSSHAIR_ENABLED.get()) return;
        if (mc.getDebugOverlay().showDebugScreen()) return;

        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        float cx = sw / 2f + Config.CROSSHAIR_OFFSET_X.get().floatValue();
        float cy = sh / 2f + Config.CROSSHAIR_OFFSET_Y.get().floatValue();
        float size = Config.CROSSHAIR_SIZE.get().floatValue();
        int color = Config.CROSSHAIR_COLOR.get();
        float opacity = Config.CROSSHAIR_OPACITY.get().floatValue();
        float rot = (float) Math.toRadians(Config.CROSSHAIR_ROTATION.get());

        if (Config.CROSSHAIR_CUSTOM_IMAGE.get() && customTexture != null) {
            renderCustomImage(g, cx, cy, size, color, opacity, rot);
        } else {
            renderBuiltIn(g, cx, cy, size, color, opacity, rot);
        }
    }

    private void renderCustomImage(GuiGraphics g, float cx, float cy, float scale, int color, float alpha, float rot) {
        int baseSize = 16;
        int w = (int) (baseSize * scale);
        int h = w;
        int x = (int) (cx - w / 2f);
        int y = (int) (cy - h / 2f);

        RenderSystem.enableBlend();
        g.pose().pushMatrix();
        g.pose().translate(cx, cy);
        g.pose().rotate(rot);
        g.pose().translate(-cx, -cy);

        float r = ((color >> 16) & 0xFF) / 255f;
        float gr = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        RenderSystem.setShaderColor(r, gr, b, alpha);
        g.blit(customTexture, x, y, 0, 0, w, h, w, h);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        g.pose().popMatrix();
        RenderSystem.disableBlend();
    }

    private void renderBuiltIn(GuiGraphics g, float cx, float cy, float scale, int color, float alpha, float rot) {
        int c = RenderUtils.withAlphaF(color, alpha);
        int outline = RenderUtils.withAlphaF(Config.CROSSHAIR_OUTLINE_COLOR.get(), alpha);
        int style = Config.CROSSHAIR_STYLE.get();
        float gap = Config.CROSSHAIR_GAP.get().floatValue() * scale;
        float len = Config.CROSSHAIR_LENGTH.get().floatValue() * scale;
        float thick = Config.CROSSHAIR_THICKNESS.get().floatValue();
        boolean dot = Config.CROSSHAIR_DOT.get();
        boolean outlineEnabled = Config.CROSSHAIR_OUTLINE.get();

        g.pose().pushMatrix();
        g.pose().translate(cx, cy);
        g.pose().rotate(rot);
        g.pose().translate(-cx, -cy);

        switch (style) {
            case 1 -> drawDot(g, cx, cy, (int) (3 * scale), c, outlineEnabled, outline);
            case 2 -> drawCircle(g, cx, cy, (int) (4 * scale), thick, c, outlineEnabled, outline);
            case 3 -> drawCross(g, cx, cy, gap, len, thick, c, outlineEnabled, outline);
            case 4 -> drawArrow(g, cx, cy, gap, len, thick, c, outlineEnabled, outline);
            case 5 -> drawBracket(g, cx, cy, gap, len, thick, c, outlineEnabled, outline);
            default -> drawStandardCross(g, cx, cy, gap, len, thick, c, outlineEnabled, outline);
        }

        if (dot && style != 1) {
            drawDot(g, cx, cy, (int) (1.5f * scale), c, outlineEnabled, outline);
        }

        g.pose().popMatrix();
    }

    private void drawStandardCross(GuiGraphics g, float cx, float cy, float gap, float len, float thick, int color, boolean outline, int outColor) {
        int t = (int) Math.max(1, thick);
        for (int i = 0; i < t; i++) {
            int o = outline ? 1 : 0;
            if (outline) {
                drawRawLine(g, cx - i - o, cy - gap - len - o, cx + i + 1 + o, cy - gap + o, outColor);
                drawRawLine(g, cx - i - o, cy + gap - o, cx + i + 1 + o, cy + gap + len + o, outColor);
                drawRawLine(g, cx - gap - len - o, cy - i - o, cx - gap + o, cy + i + 1 + o, outColor);
                drawRawLine(g, cx + gap - o, cy - i - o, cx + gap + len + o, cy + i + 1 + o, outColor);
            }
            drawRawLine(g, cx - i, cy - gap - len, cx + i + 1, cy - gap, color);
            drawRawLine(g, cx - i, cy + gap, cx + i + 1, cy + gap + len, color);
            drawRawLine(g, cx - gap - len, cy - i, cx - gap, cy + i + 1, color);
            drawRawLine(g, cx + gap, cy - i, cx + gap + len, cy + i + 1, color);
        }
    }

    private void drawCross(GuiGraphics g, float cx, float cy, float gap, float len, float thick, int color, boolean outline, int outColor) {
        drawStandardCross(g, cx, cy, gap, len, thick, color, outline, outColor);
    }

    private void drawDot(GuiGraphics g, float cx, float cy, int radius, int color, boolean outline, int outColor) {
        int x = (int) (cx - radius);
        int y = (int) (cy - radius);
        int s = radius * 2 + 1;
        if (outline) {
            g.fill(x - 1, y - 1, x + s + 1, y + s + 1, outColor);
        }
        g.fill(x, y, x + s, y + s, color);
    }

    private void drawCircle(GuiGraphics g, float cx, float cy, int radius, float thick, int color, boolean outline, int outColor) {
        int steps = 36;
        for (int i = 0; i < steps; i++) {
            double a1 = Math.PI * 2 * i / steps;
            double a2 = Math.PI * 2 * (i + 1) / steps;
            int x1 = (int) (cx + Math.cos(a1) * radius);
            int y1 = (int) (cy + Math.sin(a1) * radius);
            int x2 = (int) (cx + Math.cos(a2) * radius);
            int y2 = (int) (cy + Math.sin(a2) * radius);
            drawRawLine(g, x1, y1, x2, y2, color);
        }
    }

    private void drawArrow(GuiGraphics g, float cx, float cy, float gap, float len, float thick, int color, boolean outline, int outColor) {
        int t = (int) Math.max(1, thick);
        for (int i = 0; i < t; i++) {
            // 4 arrows pointing in
            drawRawLine(g, cx - gap - len, cy - gap - len, cx - gap, cy - gap, color);
            drawRawLine(g, cx + gap + len, cy - gap - len, cx + gap, cy - gap, color);
            drawRawLine(g, cx - gap - len, cy + gap + len, cx - gap, cy + gap, color);
            drawRawLine(g, cx + gap + len, cy + gap + len, cx + gap, cy + gap, color);
        }
    }

    private void drawBracket(GuiGraphics g, float cx, float cy, float gap, float len, float thick, int color, boolean outline, int outColor) {
        int t = (int) Math.max(1, thick);
        float g2 = gap + len;
        for (int i = 0; i < t; i++) {
            // Top-left
            drawRawLine(g, cx - g2, cy - gap - i, cx - gap - i, cy - gap - i, color);
            drawRawLine(g, cx - gap - i, cy - g2, cx - gap - i, cy - gap - i, color);
            // Top-right
            drawRawLine(g, cx + gap + i, cy - gap - i, cx + g2, cy - gap - i, color);
            drawRawLine(g, cx + gap + i, cy - g2, cx + gap + i, cy - gap - i, color);
            // Bottom-left
            drawRawLine(g, cx - g2, cy + gap + i, cx - gap - i, cy + gap + i, color);
            drawRawLine(g, cx - gap - i, cy + gap + i, cx - gap - i, cy + g2, color);
            // Bottom-right
            drawRawLine(g, cx + gap + i, cy + gap + i, cx + g2, cy + gap + i, color);
            drawRawLine(g, cx + gap + i, cy + gap + i, cx + gap + i, cy + g2, color);
        }
    }

    private void drawRawLine(GuiGraphics g, float x1, float y1, float x2, float y2, int color) {
        int ix1 = (int) x1, iy1 = (int) y1, ix2 = (int) x2, iy2 = (int) y2;
        if (ix1 == ix2) {
            g.fill(ix1, Math.min(iy1, iy2), ix1 + 1, Math.max(iy1, iy2) + 1, color);
        } else if (iy1 == iy2) {
            g.fill(Math.min(ix1, ix2), iy1, Math.max(ix1, ix2) + 1, iy1 + 1, color);
        } else {
            g.fill(ix1, iy1, ix2 + 1, iy2 + 1, color);
        }
    }

    public String getLoadedFileName() {
        return loadedFileName;
    }

    public boolean hasCustomImage() {
        return customTexture != null;
    }
}
