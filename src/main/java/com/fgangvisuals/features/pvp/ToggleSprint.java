package com.fgangvisuals.features.pvp;

import com.fgangvisuals.config.Config;
import com.fgangvisuals.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class ToggleSprint {
    private final Minecraft mc = Minecraft.getInstance();
    private boolean sprinting = false;
    private boolean sneaking = false;
    private boolean sprintKeyWasDown = false;
    private boolean sneakKeyWasDown = false;

    @SubscribeEvent
    public void onTick(PlayerTickEvent.Post event) {
        if (mc.player == null) return;

        if (Config.TOGGLE_SPRINT_ENABLED.get()) {
            boolean sprintDown = mc.options.keySprint.isDown();
            if (sprintDown && !sprintKeyWasDown) {
                sprinting = !sprinting;
            }
            sprintKeyWasDown = sprintDown;
            if (sprinting && mc.player.input.forwardImpulse > 0 && !mc.player.isSprinting()) {
                mc.player.setSprinting(true);
            }
        }

        if (Config.TOGGLE_SNEAK_ENABLED.get()) {
            boolean sneakDown = mc.options.keyShift.isDown();
            if (sneakDown && !sneakKeyWasDown) {
                sneaking = !sneaking;
            }
            sneakKeyWasDown = sneakDown;
            mc.options.keyShift.setDown(sneaking);
        }
    }

    @SubscribeEvent
    public void onRender(RenderGuiEvent.Post event) {
        if (!Config.TOGGLE_SPRINT_DISPLAY.get() || mc.player == null || mc.options.hideGui) return;
        if (!Config.TOGGLE_SPRINT_ENABLED.get() && !Config.TOGGLE_SNEAK_ENABLED.get()) return;

        GuiGraphics g = event.getGuiGraphics();
        int c = Config.TOGGLE_SPRINT_COLOR.get();
        int sw = mc.getWindow().getGuiScaledWidth();

        if (Config.TOGGLE_SPRINT_ENABLED.get() && mc.player.isSprinting()) {
            String t = "[SPRINTING]";
            g.drawString(mc.font, t, sw - mc.font.width(t) - 4, 24, c, true);
        }
        if (Config.TOGGLE_SNEAK_ENABLED.get() && mc.player.isShiftKeyDown()) {
            String t = "[SNEAKING]";
            g.drawString(mc.font, t, sw - mc.font.width(t) - 4, 36, c, true);
        }
    }

    public boolean isSprinting() { return sprinting; }
    public boolean isSneaking() { return sneaking; }
    public void toggleSprint() { sprinting = !sprinting; }
    public void toggleSneak() { sneaking = !sneaking; }
}
