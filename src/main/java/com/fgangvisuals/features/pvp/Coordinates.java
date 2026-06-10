package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class Coordinates {
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.COORDS_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.COORDS_SCALE.get().floatValue();
        int bx = Config.COORDS_X.get();
        int by = Config.COORDS_Y.get();
        int c = Config.COORDS_COLOR.get();

        g.pose().pushMatrix();
        g.pose().scale((float) scale, (float) scale);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);

        int px = (int) mc.player.getX();
        int py = (int) mc.player.getY();
        int pz = (int) mc.player.getZ();
        String coords = String.format("XYZ: %d / %d / %d", px, py, pz);

        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add(coords);
        if (Config.COORDS_DIRECTION.get()) {
            Direction f = mc.player.getDirection();
            lines.add("Facing: " + f.getName().toUpperCase().substring(0, 1));
        }
        if (Config.COORDS_BIOME.get() && mc.level != null) {
            var biome = mc.level.getBiome(mc.player.blockPosition());
            lines.add(biome.unwrapKey().map(k -> k.registry().getPath()).orElse("?"));
        }

        int mw = 0;
        for (String s : lines) mw = Math.max(mw, mc.font.width(s));
        int w = mw + 8;
        int h = lines.size() * 12 + 4;

        if (Config.COORDS_BACKGROUND.get()) RenderUtils.drawMinimalBackground(g, x, y, w, h, 0.5f);
        for (int i = 0; i < lines.size(); i++) {
            g.drawString(mc.font, lines.get(i), x + 4, y + 4 + i * 12, c, true);
        }
        g.pose().popMatrix();
    }
}
