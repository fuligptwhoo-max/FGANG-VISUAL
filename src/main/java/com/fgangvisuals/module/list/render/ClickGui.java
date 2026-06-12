package com.fgangvisuals.module.list.render;

import org.lwjgl.glfw.GLFW;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.ui.ClickGuiFrame;
import com.fgangvisuals.ui.Panel;

@ModuleInformation(moduleName = "Click Gui", moduleCategory = ModuleCategory.RENDER, moduleKeybind = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGui extends Module {

    private ClickGuiFrame clickGuiFrame;

    @Override
    public void onEnable() {
        if (clickGuiFrame == null) clickGuiFrame = new ClickGuiFrame();
        mc.setScreen(clickGuiFrame);
        for (Panel panel : clickGuiFrame.getPanels()) {
            panel.getAnimationAlpha().setValue(0);
            panel.getAnimationAlpha().setStartValue(0);
            panel.getAnimationAlpha().reset();
        }
        toggle();
    }
}
