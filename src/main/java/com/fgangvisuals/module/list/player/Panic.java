package com.fgangvisuals.module.list.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Formatting;
import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.BindSetting;

@Getter
@ModuleInformation(moduleName = "Unhook", moduleCategory = ModuleCategory.MISC)
public class Panic extends Module {

    private final BindSetting backKey = new BindSetting("Кнопка возврата",-1);

    @Setter
    private boolean hide;

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player == null) return;
        if (backKey.getValue() == -1) {
            logDirect(Formatting.RED + "Забиндите кнопку возврата!");
            setEnabled(false);
            return;
        }
        setHide(true);
        mc.player.closeScreen();
    }
}