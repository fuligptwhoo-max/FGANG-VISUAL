package com.fgangvisuals.module.list.render;

import com.fgangvisuals.module.Module;
import com.fgangvisuals.module.ModuleCategory;
import com.fgangvisuals.module.ModuleInformation;
import com.fgangvisuals.module.settings.SliderSetting;
import com.fgangvisuals.util.base.Instance;

@ModuleInformation(moduleName = "Aspect Ratio", moduleDesc = "Соотношение сторон", moduleCategory = ModuleCategory.RENDER)
public class AspectRatio extends Module {

    public final SliderSetting multiplier = new SliderSetting("Множитель", 1.33f, 1.0f, 2.5f, 0.01f);

    public float getAspectMultiplier() {
        return multiplier.getFloatValue();
    }

    public static AspectRatio getInstance() {
        return Instance.get(AspectRatio.class);
    }

}
