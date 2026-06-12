package com.fgangvisuals.module.list.player;

import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.SliderSetting;

@ModuleInformation(moduleName = "Item Scroller", moduleDesc = "Шифт+ЛКМ+вод по предметам", moduleCategory = ModuleCategory.PLAYER)
public class ItemScroller extends Module {

    private final SliderSetting delaySetting = new SliderSetting("Задержка", 0.0f, 0.0f, 150.0f, 1.0f);

    public long getDelay() {
        return (long) delaySetting.getFloatValue();
    }
}
