package com.fgangvisuals.gui;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class Notifications {
    private final Minecraft mc = Minecraft.getInstance();
    private final List<Note> notes = new ArrayList<>();

    private static class Note {
        String text; long time;
        Note(String t) { this.text = t; this.time = System.currentTimeMillis(); }
    }

    public void show(String text) {
        if (!Config.NOTIFICATIONS_ENABLED.get()) return;
        notes.add(new Note(text));
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.NOTIFICATIONS_ENABLED.get() || mc.player == null) return;
        notes.removeIf(n -> System.currentTimeMillis() - n.time > Config.NOTIFICATIONS_DURATION.get());
        if (notes.isEmpty()) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.NOTIFICATIONS_SCALE.get().floatValue();
        int bx = Config.NOTIFICATIONS_X.get();
        int by = Config.NOTIFICATIONS_Y.get();

        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        int h = 16;
        for (int i = 0; i < notes.size(); i++) {
            Note n = notes.get(i);
            int w = mc.font.width(n.text) + 8;
            float age = (System.currentTimeMillis() - n.time) / (float) Config.NOTIFICATIONS_DURATION.get();
            int alpha = (int) ((1f - age) * 200);
            int bg = (alpha << 24) | 0x111111;
            int border = (alpha << 24) | 0x333333;
            g.fill(x, y + i * h, x + w, y + i * h + h - 2, bg);
            g.fill(x, y + i * h, x + w, y + i * h + 1, border);
            g.drawString(mc.font, n.text, x + 4, y + i * h + 3, 0xFFFFFFFF, true);
        }
        g.pose().popPose();
    }
}
