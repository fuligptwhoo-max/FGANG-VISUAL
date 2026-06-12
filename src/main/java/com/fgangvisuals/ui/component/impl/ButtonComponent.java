package com.fgangvisuals.ui.component.impl;

import net.minecraft.client.util.math.MatrixStack;
import com.fgangvisuals.module.settings.ButtonSetting;
import com.fgangvisuals.ui.component.Component;
import com.fgangvisuals.util.cursor.CursorManager;
import com.fgangvisuals.util.render.helper.HoverUtil;
import com.fgangvisuals.util.render.msdf.Fonts;
import com.fgangvisuals.util.render.providers.ColorProvider;
import com.fgangvisuals.util.render.renderers.DrawUtil;

public class ButtonComponent extends Component {

    private final ButtonSetting setting;

    public ButtonComponent(ButtonSetting setting) {
        this.setting = setting;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        float animValue = getAlphaAnimSetting().getValue();
        float alpha = Math.max(Math.min(animValue * getAlphaAnim().getValue(), 1), 0);
        int alphaInt = (int) (255 * alpha);

        if (alpha < 0.02f) {
            setHeight(0);
            return;
        }

        float btnHeight = 11f;
        float btnY = y + 1.5f;
        float btnPadding = 4f;
        float textWidth = Fonts.SFREGULAR.get().getWidth(setting.getButtonText(), 6f);
        float btnWidth = textWidth + btnPadding * 2;
        float btnX = x + width - btnWidth - 2.5f;

        boolean hovered = HoverUtil.isHovered(mouseX, mouseY, btnX, btnY, btnWidth, btnHeight);
        if (hovered) CursorManager.requestHand();

        int bgColor = hovered
                ? ColorProvider.rgba(70, 70, 75, (int)(180 * alpha))
                : ColorProvider.rgba(55, 55, 60, (int)(150 * alpha));

        DrawUtil.drawRound(btnX - 0.5f, btnY - 0.5f, btnWidth + 1, btnHeight + 1, 2f, ColorProvider.rgba(80, 80, 85, (int)(120 * alpha)));
        DrawUtil.drawRound(btnX, btnY, btnWidth, btnHeight, 2f, bgColor);

        DrawUtil.drawText(Fonts.SFREGULAR.get(), setting.getName(), x + 4.5f, y + 3.5f, ColorProvider.rgba(255, 255, 255, alphaInt), 6.5f);
        DrawUtil.drawText(Fonts.SFREGULAR.get(), setting.getButtonText(), btnX + btnPadding, btnY + 2.5f, ColorProvider.rgba(220, 220, 220, alphaInt), 6f);

        setHeight(14);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return;

        float btnHeight = 11f;
        float btnY = y + 1.5f;
        float btnPadding = 4f;
        float textWidth = Fonts.SFREGULAR.get().getWidth(setting.getButtonText(), 6f);
        float btnWidth = textWidth + btnPadding * 2;
        float btnX = x + width - btnWidth - 2.5f;

        if (HoverUtil.isHovered(mouseX, mouseY, btnX, btnY, btnWidth, btnHeight)) {
            setting.click();
        }
    }

    @Override
    public boolean isVisible() {
        return setting.visible.get();
    }
}
