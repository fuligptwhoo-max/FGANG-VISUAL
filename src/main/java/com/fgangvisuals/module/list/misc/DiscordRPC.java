package com.fgangvisuals.module.list.misc;

import com.google.common.eventbus.Subscribe;
import meteordevelopment.discordipc.DiscordIPC;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import com.fgangvisuals.event.list.EventTick;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.util.discord.ExtendedRichPresence;

@ModuleInformation(moduleName = "Discord RPC", moduleDesc = "Discord Rich Presence", moduleCategory = ModuleCategory.MISC)
public class DiscordRPC extends Module {

    private final ExtendedRichPresence rpc = new ExtendedRichPresence();
    private static final long APPLICATION_ID = 1511427759170261232L;
    private boolean buttonsAdded = false;
    private String lastDetails = "";

    @Override
    public void onEnable() {
        super.onEnable();
        DiscordIPC.start(APPLICATION_ID, null);
        rpc.setStart(System.currentTimeMillis() / 1000);
        buttonsAdded = false;
        lastDetails = "";
    }

    @Override
    public void onDisable() {
        super.onDisable();
        DiscordIPC.stop();
    }

    @Subscribe
    private void onTick(EventTick e) {
        String details;

        if (mc.player != null) {
            if (mc.getCurrentServerEntry() != null && mc.getCurrentServerEntry().address != null) {
                details = "На сервере: " + mc.getCurrentServerEntry().address;
            } else if (mc.isInSingleplayer()) {
                details = "В одиночной игре";
            } else {
                details = "В игре";
            }
        } else if (mc.currentScreen != null) {
            String screenName = mc.currentScreen.getClass().getSimpleName();

            if (mc.currentScreen instanceof TitleScreen) {
                details = "В главном меню";
            } else if (mc.currentScreen instanceof MultiplayerScreen) {
                details = "В списке серверов";
            } else if (mc.currentScreen instanceof OptionsScreen) {
                details = "В настройках";
            } else if (screenName.contains("Alt") || screenName.contains("Account")) {
                details = "Выбирает аккаунт";
            } else {
                details = "В главном меню";
            }
        } else {
            details = "В главном меню";
        }

        if (details.equals(lastDetails)) return;
        lastDetails = details;

        rpc.setDetails(details);
        rpc.setState("chekvisual");
        rpc.setLargeImage("logo", "MoonWard 1.21.4");

        if (!buttonsAdded) {
            rpc.addButton("Сайт визуалов", "https://chekvisual.netlify.app/");
            buttonsAdded = true;
        }

        DiscordIPC.setActivity(rpc);
    }
}
