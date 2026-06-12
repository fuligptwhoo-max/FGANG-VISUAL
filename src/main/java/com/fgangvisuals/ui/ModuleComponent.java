package com.fgangvisuals.ui;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.settings.*;
import com.fgangvisuals.ui.component.Component;
import com.fgangvisuals.ui.component.impl.*;
import com.fgangvisuals.module.settings.ZoneListSetting;
import com.fgangvisuals.ui.component.impl.ZoneListComponent;
import com.fgangvisuals.util.cursor.CursorManager;
import com.fgangvisuals.util.render.helper.HoverUtil;
import com.fgangvisuals.util.render.math.Animation;
import com.fgangvisuals.util.render.math.Easing;
import com.fgangvisuals.util.render.msdf.Fonts;
import com.fgangvisuals.util.render.providers.ColorProvider;
import com.fgangvisuals.util.render.renderers.DrawUtil;

@Getter
public class ModuleComponent extends Component {
    private final Module module;
    private final Panel panel;

    private final Animation animation = new Animation(Easing.QUINTIC_OUT, 320);
    private final Animation hoverAnim = new Animation(Easing.QUINTIC_OUT, 300);
    private final Animation enabledAnim = new Animation(Easing.QUINTIC_OUT, 400);

    public boolean open;
    private boolean isHovered;
    private boolean binding;

    private final ObjectArrayList<Component> components = new ObjectArrayList<>();

    public ModuleComponent(Module module, Panel panel) {
        this.module = module;
        this.panel = panel;
        for (Setting setting : module.getSettings()) {
            switch (setting) {
                case BooleanSetting option -> components.add(new BooleanComponent(option));
                case ModeSetting option -> components.add(new ModeComponent(option));
                case ModeListSetting option -> components.add(new ModeListComponent(option));
                case SliderSetting option -> components.add(new SliderComponent(option));
                case BindSetting option -> components.add(new BindComponent(option));
                case ThemeSetting option -> components.add(new ThemeComponent(option));
                case ColorSetting option -> components.add(new ColorPickerComponent(option));
                case ZoneListSetting option -> components.add(new ZoneListComponent(option));
                case ItemListSetting option -> components.add(new ItemListComponent(option));
                default -> {}
            }
        }

    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        isHovered = HoverUtil.isHovered(mouseX, mouseY, x, y, width, 15);

        hoverAnim.run(isHovered);
        animation.run(open);
        enabledAnim.run(module.isEnabled());

        if (HoverUtil.isHovered(mouseX, mouseY, x, y, width, 15)) CursorManager.requestHand();

        float alpha = Math.max(Math.min(panel.getAnimationAlpha().getValue(), 1), 0);

        int textColor = ColorProvider.interpolateColor(
                ColorProvider.rgba(170, 170, 170, (int)(255 * alpha)),
                ColorProvider.rgba(255, 255, 255, (int)(255 * alpha)),
                enabledAnim.getValue()
        );

        float highlightProgress = Math.max(hoverAnim.getValue(), enabledAnim.getValue());
        int outlineAlpha = (int) ((25 + (40 * highlightProgress)) * alpha);

        int outlineColor = ColorProvider.rgba(255, 255, 255, outlineAlpha);
        int innerColor = ColorProvider.rgba(44, 44, 44, (int)(140 * alpha));

        float currentHeight = 15f + ((height - 15f) * animation.getValue());

        DrawUtil.drawRound(x - 0.5f, y - 0.5f, width + 1f, currentHeight + 0.5f, 3.5f, outlineColor);
        DrawUtil.drawRoundBlur(x, y, width, currentHeight - 0.5f, 3f, innerColor, 20f);

        if (binding) {
            DrawUtil.drawText(Fonts.SFREGULAR.get(), "Нажмите клавишу...", x + width / 2f - Fonts.SFREGULAR.get().getWidth("Нажмите клавишу...", 7.5f) / 2f, y + 3.5f, ColorProvider.rgba(255, 255, 255, (int)(255 * alpha)), 7.5f);
        } else {
            DrawUtil.drawText(Fonts.SFREGULAR.get(), module.getName(), x + 4.5f, y + 3.75f, textColor, 7.5f);

            if (!components.isEmpty()) {
                DrawUtil.drawText(Fonts.SFMEDIUM.get(), "...", x + width - 12, y + 1.75f, textColor, 7.5f);
            }
        }

        if (animation.getValue() > 0.01f) {
            float compY = y + 13.5f;
            float panelTop = panel.getY() + 20;
            float panelBottom = panel.getY() + panel.getHeight() - 4;
            float settingsY = y + 15;
            float settingsBottom = y + currentHeight;

            float intersectY = Math.max(settingsY, panelTop);
            float intersectBottom = Math.min(settingsBottom, panelBottom);
            float intersectHeight = Math.max(0, intersectBottom - intersectY);

            float darkHeight = currentHeight - 15f;
            if (darkHeight > 0) {
                DrawUtil.drawRound(x + 1f, y + 15, width - 2f, darkHeight, 0f, ColorProvider.rgba(0, 0, 0, (int)(30 * alpha * animation.getValue())));
            }

            for (Component component : components) {
                component.getAlphaAnim().setValue(Math.min(panel.getAnimationAlpha().getValue(), 1));
                component.getAlphaAnimSetting().run(component.isVisible());

                float visibleProgress = MathHelper.clamp(component.getAlphaAnimSetting().getValue(), 0f, 1f);
                if (component.isVisible() || visibleProgress > 0) {
                    component.setX(x);
                    component.setY(compY);
                    component.setWidth(width - 4);

                    com.fgangvisuals.util.render.math.Scissor.push();
                    com.fgangvisuals.util.render.math.Scissor.setFromComponentCoordinates(x, intersectY, width, intersectHeight);

                    component.render(matrixStack, mouseX, mouseY, partialTicks);

                    com.fgangvisuals.util.render.math.Scissor.unset();
                    com.fgangvisuals.util.render.math.Scissor.pop();

                    compY += component.getHeight() * visibleProgress;
                }
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY, 15)) {
            if (button == 0) module.setEnabled(!module.isEnabled());
            if (button == 1 && !components.isEmpty()) open = !open;
            if (button == 2) binding = !binding;
        }

        if (open) {
            for (Component component : components) {
                if (component.isVisible() && component.getAlphaAnimSetting().getValue() > 0.5f) {
                    component.mouseClicked(mouseX, mouseY, button);
                }
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (open) {
            for (Component component : components) {
                component.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_DELETE) {
                module.setKey(-1);
            } else {
                module.setKey(keyCode);
            }
            binding = false;
            return;
        }

        if (open) {
            for (Component component : components) {
                component.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    private boolean isHovered(double mouseX, double mouseY, float heightCheck) {
        return HoverUtil.isHovered(mouseX, mouseY, x, y, width, heightCheck);
    }
}