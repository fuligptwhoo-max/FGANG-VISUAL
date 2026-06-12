package com.fgangvisuals.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.fgangvisuals.util.replace.ReplaceUtil;

@Mixin(Team.class)
public class TeamMixin {

    @ModifyArg(
            method = "decorateName(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/MutableText;append(Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;",
                    ordinal = 0
            ),
            index = 0
    )
    private Text modify(Text original) {
        return original;
    }
}