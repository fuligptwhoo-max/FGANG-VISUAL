package com.fgangvisuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fgangvisuals.module.list.render.ClickGui;

public class FGANGVisualsClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("fgangvisuals-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("FGANGVisuals client entrypoint initialized");

        KeyBinding clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fgangvisuals.clickgui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.fgangvisuals"
        ));

        LOGGER.info("Registered ClickGUI keybinding: {}", clickGuiKey.getTranslationKey());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClickGui clickGui = FGANGVisuals.getInstance().getModuleStorage().get(ClickGui.class);
            if (clickGui != null) {
                clickGui.setKey(clickGuiKey.getDefaultKey().getCode());
            }
        });
    }
}
