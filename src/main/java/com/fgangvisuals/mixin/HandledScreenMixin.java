package com.fgangvisuals.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.fgangvisuals.FGANGVisuals;
import com.fgangvisuals.event.list.EventHandledScreen;
import com.fgangvisuals.event.list.EventSlotClick;
import com.fgangvisuals.module.list.player.ItemScroller;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Shadow
    protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Shadow
    protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    @Unique
    private long lastScrollTime = 0;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        FGANGVisuals.getInstance().getEventBus().post(new EventHandledScreen(focusedSlot));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderTail(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;

        ItemScroller scroller = com.fgangvisuals.util.base.Instance.get(ItemScroller.class);
        if (scroller == null || !scroller.isEnabled()) return;

        long window = mc.getWindow().getHandle();
        if (!InputUtil.isKeyPressed(window, 340) && !InputUtil.isKeyPressed(window, 344)) return;
        if (GLFW.glfwGetMouseButton(window, 0) != 1) return;

        long now = System.currentTimeMillis();
        if (now - lastScrollTime < scroller.getDelay()) return;

        for (Slot slot : mc.player.currentScreenHandler.slots) {
            if (slot.isEnabled() && isPointOverSlot(slot, mouseX, mouseY) && slot.hasStack()) {
                onMouseClick(slot, slot.id, 0, SlotActionType.QUICK_MOVE);
                lastScrollTime = now;
                break;
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        FGANGVisuals.getInstance().getEventBus().post(new EventSlotClick(focusedSlot, button));
    }
}
