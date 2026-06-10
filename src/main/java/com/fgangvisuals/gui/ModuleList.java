package com.fgangvisuals.gui;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.ColorUtils;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class ModuleList {
    private final Minecraft mc = Minecraft.getInstance();
    private final List<String> modules = new ArrayList<>();

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.ARRAYLIST_ENABLED.get() || mc.player == null || mc.options.hideGui) return;

        updateModules();
        if (modules.isEmpty()) return;

        GuiGraphics g = event.getGuiGraphics();
        float scale = Config.ARRAYLIST_SCALE.get().floatValue();
        int bx = Config.ARRAYLIST_X.get();
        int by = Config.ARRAYLIST_Y.get();
        int c = Config.ARRAYLIST_COLOR.get();

        g.pose().pushPose();
        g.pose().scale(scale, scale, 1f);
        int x = (int) (bx / scale);
        int y = (int) (by / scale);
        int h = 12;

        modules.sort((a, b) -> Integer.compare(mc.font.width(b), mc.font.width(a)));

        for (int i = 0; i < modules.size(); i++) {
            String m = modules.get(i);
            int w = mc.font.width(m) + 6;
            int my = y + i * h;
            int color = c;
            if (Config.ARRAYLIST_RAINBOW.get()) {
                color = ColorUtils.getRainbow(0.7f, 1f, i * 100L);
            }
            if (Config.ARRAYLIST_BACKGROUND.get()) {
                RenderUtils.drawRect(g, x, my, w, h, RenderUtils.withAlphaF(0x111111, 0.5f));
            }
            g.drawString(mc.font, m, x + 3, my + 2, color, true);
        }
        g.pose().popPose();
    }

    private void updateModules() {
        modules.clear();
        if (Config.ARMOR_HUD_ENABLED.get()) modules.add("ArmorHUD");
        if (Config.POTION_HUD_ENABLED.get()) modules.add("PotionHUD");
        if (Config.KEYSTROKES_ENABLED.get()) modules.add("KeyStrokes");
        if (Config.CROSSHAIR_ENABLED.get()) modules.add("Crosshair");
        if (Config.HIT_COLOR_ENABLED.get()) modules.add("HitColor");
        if (Config.TOGGLE_SPRINT_ENABLED.get()) modules.add("ToggleSprint");
        if (Config.CPS_ENABLED.get()) modules.add("CPS");
        if (Config.COORDS_ENABLED.get()) modules.add("Coords");
        if (Config.FPS_ENABLED.get()) modules.add("FPS");
        if (Config.PING_ENABLED.get()) modules.add("Ping");
        if (Config.TARGET_HUD_ENABLED.get()) modules.add("TargetHUD");
        if (Config.REACH_ENABLED.get()) modules.add("Reach");
        if (Config.COMBO_ENABLED.get()) modules.add("Combo");
        if (Config.FULLBRIGHT_ENABLED.get()) modules.add("FullBright");
        if (Config.NO_HURT_CAM_ENABLED.get()) modules.add("NoHurtCam");
        if (Config.BLOCK_OVERLAY_ENABLED.get()) modules.add("BlockOverlay");
        if (Config.INV_HUD_ENABLED.get()) modules.add("InvHUD");
    }
}
