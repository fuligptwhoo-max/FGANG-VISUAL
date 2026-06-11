package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class KeyStrokes {
    private final Minecraft mc = Minecraft.getInstance();

    private static class KeyInfo {
        final String label;
        final int keyCode;
        final int rx, ry;
        final int rw, rh;
        KeyInfo(String l, int k, int rx, int ry, int rw, int rh) {
            this.label = l; this.keyCode = k; this.rx = rx; this.ry = ry; this.rw = rw; this.rh = rh;
        }
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.KEYSTROKES_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.KEYSTROKES_SCALE.get().floatValue();
        int bx = Config.KEYSTROKES_X.get();
        int by = Config.KEYSTROKES_Y.get();
        int c = Config.KEYSTROKES_COLOR.get();
        int pc = Config.KEYSTROKES_PRESSED_COLOR.get();

        g.pose().pushPose();
        g.pose().scale((float) scale, (float) scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        int s = 24;
        int gap = 2;

        java.util.List<KeyInfo> keys = new java.util.ArrayList<>();
        keys.add(new KeyInfo("W", GLFW.GLFW_KEY_W, x + s + gap, y, s, s));
        keys.add(new KeyInfo("A", GLFW.GLFW_KEY_A, x, y + s + gap, s, s));
        keys.add(new KeyInfo("S", GLFW.GLFW_KEY_S, x + s + gap, y + s + gap, s, s));
        keys.add(new KeyInfo("D", GLFW.GLFW_KEY_D, x + (s + gap) * 2, y + s + gap, s, s));

        if (Config.KEYSTROKES_MOUSE.get()) {
            int mw = (int) ((s * 1.5f));
            keys.add(new KeyInfo("LMB", -100, x, y + (s + gap) * 2, mw, s));
            keys.add(new KeyInfo("RMB", -99, x + mw + gap, y + (s + gap) * 2, mw, s));
        }
        if (Config.KEYSTROKES_SPACE.get()) {
            int sw = s * 3 + gap * 2;
            keys.add(new KeyInfo("—", GLFW.GLFW_KEY_SPACE, x, y + (s + gap) * (Config.KEYSTROKES_MOUSE.get() ? 3 : 2), sw, s));
        }

        for (KeyInfo k : keys) {
            boolean pressed = isPressed(k.keyCode);
            int bg = pressed ? pc : 0x22000000;
            int border = pressed ? 0xFFFFFFFF : RenderUtils.withAlphaF(c, 0.5f);
            int tc = pressed ? 0xFF000000 : c;

            if (Config.KEYSTROKES_BACKGROUND.get()) {
                g.fill(k.rx, k.ry, k.rx + k.rw, k.ry + k.rh, bg);
                RenderUtils.drawBorder(g, k.rx, k.ry, k.rw, k.rh, border);
            }
            String lbl = pressed ? "§0" + k.label : "§7" + k.label;
            int tx = k.rx + k.rw / 2 - mc.font.width(lbl) / 2;
            int ty = k.ry + k.rh / 2 - 4;
            g.drawString(mc.font, lbl, tx, ty, tc, false);
        }
        g.pose().popPose();
    }

    private boolean isPressed(int code) {
        if (code == -100) return mc.mouseHandler.isLeftPressed();
        if (code == -99) return mc.mouseHandler.isRightPressed();
        return InputConstants.isKeyDown(mc.getWindow().getWindow(), code);
    }
}
