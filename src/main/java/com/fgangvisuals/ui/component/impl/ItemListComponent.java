package com.fgangvisuals.ui.component.impl;

import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import com.fgangvisuals.module.list.player.ServerHelperItem;
import com.fgangvisuals.module.settings.ItemListSetting;
import com.fgangvisuals.ui.component.Component;
import com.fgangvisuals.util.config.ConfigManager;
import com.fgangvisuals.util.cursor.CursorManager;
import com.fgangvisuals.util.keyboard.KeyStorage;
import com.fgangvisuals.util.render.helper.HoverUtil;
import com.fgangvisuals.util.render.math.Animation;
import com.fgangvisuals.util.render.math.Easing;
import com.fgangvisuals.util.render.msdf.Fonts;
import com.fgangvisuals.util.render.providers.ColorProvider;
import com.fgangvisuals.util.render.renderers.DrawUtil;

import java.util.List;

public class ItemListComponent extends Component {

    private final ItemListSetting setting;
    private final Animation openAnim = new Animation(Easing.QUINTIC_OUT, 300);
    private int bindingIndex = -1;

    public ItemListComponent(ItemListSetting setting) {
        this.setting = setting;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        float alpha = Math.min(getAlphaAnimSetting().getValue(), 1);
        int alphaInt = (int) (255 * alpha);

        float yOff = y;

        DrawUtil.drawText(Fonts.SFREGULAR.get(), setting.getName() + " (" + setting.size() + ")", x + 4.5f, yOff + 3, ColorProvider.rgba(255, 255, 255, alphaInt), 6.5f);

        yOff += 11;

        List<ServerHelperItem> list = setting.getValue();
        for (int i = 0; i < list.size(); i++) {
            ServerHelperItem item = list.get(i);

            float itemAlpha = alpha * (1f - 0.15f * i);
            int ia = (int) (255 * itemAlpha);

            String displayName = item.itemId.isEmpty() ? "?" : item.itemId;
            if (displayName.contains(":")) displayName = displayName.split(":")[1];

            String text = i + ": " + displayName;
            float textWidth = Fonts.SFREGULAR.get().getWidth(text, 5.5f);

            // Bind box
            String bindStr = bindingIndex == i ? "..." : (item.key == -1 ? "Нет" : KeyStorage.getReverseKey(item.key));
            float bindWidth = Fonts.SFREGULAR.get().getWidth(bindStr, 5.5f) + 6f;

            float rmX = x + width - 12f;
            float bindX = rmX - bindWidth - 4f;
            float rmY = yOff + 1;
            float bindY = rmY;

            // Item name
            DrawUtil.drawText(Fonts.SFREGULAR.get(), text, x + 6f, yOff + 2, ColorProvider.rgba(200, 200, 200, ia), 5.5f);

            // Bind background
            boolean bindHovered = HoverUtil.isHovered(mouseX, mouseY, bindX, bindY, bindWidth, 9);
            if (bindHovered) CursorManager.requestHand();
            int bindBg = bindingIndex == i ? ColorProvider.rgba(40, 80, 40, ia) : (bindHovered ? ColorProvider.rgba(50, 50, 55, ia) : ColorProvider.rgba(30, 30, 35, ia));
            DrawUtil.drawRound(bindX, bindY, bindWidth, 9, 2f, bindBg);
            DrawUtil.drawText(Fonts.SFREGULAR.get(), bindStr, bindX + 3f, bindY + 0.5f, ColorProvider.rgba(200, 200, 200, ia), 5.5f);

            // Delete button
            boolean rmHovered = HoverUtil.isHovered(mouseX, mouseY, rmX, rmY, 10, 10);
            if (rmHovered) CursorManager.requestHand();
            int rmColor = rmHovered ? ColorProvider.rgba(255, 80, 80, ia) : ColorProvider.rgba(255, 150, 150, ia);
            DrawUtil.drawText(Fonts.SFREGULAR.get(), "×", rmX, rmY, rmColor, 6f);

            yOff += 10;
        }

        float addX = x + 6f;
        float addY = yOff + 2;
        boolean addHovered = HoverUtil.isHovered(mouseX, mouseY, addX, addY, 50, 10);
        if (addHovered) CursorManager.requestHand();
        int addColor = addHovered ? ColorProvider.rgba(100, 255, 100, alphaInt) : ColorProvider.rgba(150, 255, 150, alphaInt);
        DrawUtil.drawText(Fonts.SFREGULAR.get(), "+ Add item", addX, addY, addColor, 6f);

        float clearX = addX + 65f;
        boolean clearHovered = HoverUtil.isHovered(mouseX, mouseY, clearX, addY, 50, 10);
        if (clearHovered) CursorManager.requestHand();
        int clearColor = clearHovered ? ColorProvider.rgba(255, 100, 100, alphaInt) : ColorProvider.rgba(255, 150, 150, alphaInt);
        DrawUtil.drawText(Fonts.SFREGULAR.get(), "Clear all", clearX, addY, clearColor, 6f);

        float bottom = addY + 12;
        setHeight(bottom - y);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return;

        float yOff = y + 11;
        List<ServerHelperItem> list = setting.getValue();

        for (int i = 0; i < list.size(); i++) {
            ServerHelperItem item = list.get(i);

            String bindStr = bindingIndex == i ? "..." : (item.key == -1 ? "Нет" : KeyStorage.getReverseKey(item.key));
            float bindWidth = Fonts.SFREGULAR.get().getWidth(bindStr, 5.5f) + 6f;
            float rmX = x + width - 12f;
            float bindX = rmX - bindWidth - 4f;
            float rmY = yOff + 1;
            float bindY = rmY;

            // Bind click
            if (HoverUtil.isHovered(mouseX, mouseY, bindX, bindY, bindWidth, 9)) {
                bindingIndex = bindingIndex == i ? -1 : i;
                return;
            }

            // Delete click
            if (HoverUtil.isHovered(mouseX, mouseY, rmX, rmY, 10, 10)) {
                setting.remove(i);
                bindingIndex = -1;
                return;
            }

            yOff += 10;
        }

        float addX = x + 6f;
        float addY = yOff + 2;
        if (HoverUtil.isHovered(mouseX, mouseY, addX, addY, 60, 12)) {
            if (net.minecraft.client.MinecraftClient.getInstance().currentScreen instanceof com.fgangvisuals.ui.ClickGuiFrame) {
                net.minecraft.client.MinecraftClient.getInstance().setScreen(new net.minecraft.client.gui.screen.ingame.InventoryScreen(net.minecraft.client.MinecraftClient.getInstance().player));
                com.fgangvisuals.FGANGVisuals.getInstance().getModuleStorage().get(com.fgangvisuals.module.list.player.ServerHelper.class).pickingItem = true;
            }
            return;
        }

        float clearX = addX + 65f;
        if (HoverUtil.isHovered(mouseX, mouseY, clearX, addY, 60, 12)) {
            setting.clear();
            bindingIndex = -1;
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingIndex >= 0 && bindingIndex < setting.size()) {
            ServerHelperItem item = setting.get(bindingIndex);
            if (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_ESCAPE) {
                item.key = -1;
            } else {
                item.key = keyCode;
            }
            bindingIndex = -1;
            ConfigManager.autoSave();
        }
    }

    @Override
    public boolean isVisible() {
        return setting.visible.get();
    }
}
