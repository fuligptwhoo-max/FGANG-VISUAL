package com.fgangvisuals.ui.component.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import com.fgangvisuals.module.list.misc.ZoneData;
import com.fgangvisuals.module.settings.ZoneListSetting;
import com.fgangvisuals.ui.component.Component;
import com.fgangvisuals.ui.ClickGuiFrame;
import com.fgangvisuals.util.cursor.CursorManager;
import com.fgangvisuals.util.render.helper.HoverUtil;
import com.fgangvisuals.util.render.math.Animation;
import com.fgangvisuals.util.render.math.Easing;
import com.fgangvisuals.util.render.msdf.Fonts;
import com.fgangvisuals.util.render.providers.ColorProvider;
import com.fgangvisuals.util.render.renderers.DrawUtil;

import java.util.List;

public class ZoneListComponent extends Component {

    private final ZoneListSetting setting;
    private final Animation openAnim = new Animation(Easing.QUINTIC_OUT, 300);

    public ZoneListComponent(ZoneListSetting setting) {
        this.setting = setting;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        float alpha = Math.min(getAlphaAnimSetting().getValue(), 1);
        int alphaInt = (int) (255 * alpha);

        float yOff = y;

        DrawUtil.drawText(Fonts.SFREGULAR.get(), setting.getName() + " (" + setting.size() + ")", x + 4.5f, yOff + 3, ColorProvider.rgba(255, 255, 255, alphaInt), 6.5f);

        yOff += 11;

        List<ZoneData> list = setting.getValue();
        for (int i = 0; i < list.size(); i++) {
            ZoneData z = list.get(i);

            float zoneAlpha = alpha * (1f - 0.15f * i);
            int za = (int) (255 * zoneAlpha);

            String text = i + ": [" + z.warpCommand + "] " + z.minX + " " + z.minZ + " -> " + z.maxX + " " + z.maxZ;
            DrawUtil.drawText(Fonts.SFREGULAR.get(), text, x + 6f, yOff + 2, ColorProvider.rgba(200, 200, 200, za), 5.5f);

            float rmX = x + width - 12f;
            float rmY = yOff + 1;
            boolean rmHovered = HoverUtil.isHovered(mouseX, mouseY, rmX, rmY, 10, 10);
            if (rmHovered) CursorManager.requestHand();
            int rmColor = rmHovered ? ColorProvider.rgba(255, 80, 80, za) : ColorProvider.rgba(255, 150, 150, za);
            DrawUtil.drawText(Fonts.SFREGULAR.get(), "×", rmX, rmY, rmColor, 6f);

            yOff += 10;
        }

        float addX = x + 6f;
        float addY = yOff + 2;
        boolean addHovered = HoverUtil.isHovered(mouseX, mouseY, addX, addY, 40, 10);
        if (addHovered) CursorManager.requestHand();
        int addColor = addHovered ? ColorProvider.rgba(100, 255, 100, alphaInt) : ColorProvider.rgba(150, 255, 150, alphaInt);
        DrawUtil.drawText(Fonts.SFREGULAR.get(), "+ Add zone", addX, addY, addColor, 6f);

        float clearX = addX + 60f;
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
        for (int i = 0; i < setting.size(); i++) {
            float rmX = x + width - 12f;
            float rmY = yOff + 1;
            if (HoverUtil.isHovered(mouseX, mouseY, rmX, rmY, 10, 10)) {
                setting.remove(i);
                return;
            }
            yOff += 10;
        }

        float addX = x + 6f;
        float addY = yOff + 2;
        if (HoverUtil.isHovered(mouseX, mouseY, addX, addY, 60, 12)) {
            if (MinecraftClient.getInstance().currentScreen instanceof ClickGuiFrame) {
                MinecraftClient.getInstance().setScreen(new net.minecraft.client.gui.screen.ChatScreen(".zone add "));
            }
            return;
        }

        float clearX = addX + 60f;
        if (HoverUtil.isHovered(mouseX, mouseY, clearX, addY, 60, 12)) {
            setting.getValue().clear();
        }
    }

    @Override
    public boolean isVisible() {
        return setting.visible.get();
    }
}
