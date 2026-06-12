package com.fgangvisuals.util.commands.defaults;

import net.minecraft.client.MinecraftClient;
import com.fgangvisuals.util.commands.api.Command;
import com.fgangvisuals.util.commands.api.argument.IArgConsumer;
import com.fgangvisuals.util.commands.api.exception.CommandException;
import com.fgangvisuals.util.rotation.Rotation;
import com.fgangvisuals.util.rotation.RotationComponent;

import java.util.List;
import java.util.stream.Stream;

public class RotationCommand extends Command {
    public RotationCommand() {
        super("r");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        args.requireMin(2);
        String input = args.getString();
        if (args.hasExactlyOne()) {
            String arg = args.getString();
            if (input.contains("yaw")) {
                RotationComponent.getInstance().stopRotation();
                RotationComponent.update(new Rotation(Float.parseFloat(arg), MinecraftClient.getInstance().player.getPitch()), 360, 360, 360, 360, 0, 999999, true);
            }

            if (input.contains("pitch")) {
                RotationComponent.getInstance().stopRotation();
                RotationComponent.update(new Rotation(MinecraftClient.getInstance().player.getYaw(), Float.parseFloat(arg)), 360, 360, 360, 360, 0, 999999, true);
            }
        }
    }

    @Override
    public String getShortDesc() {
        return "Ставит ротацию";
    }

    @Override
    public List<String> getLongDesc() {
        return List.of(
                "Test"
        );
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        if (args.hasExactlyOne()) {
            return Stream.of("yaw", "pitch");
        }
        return Stream.empty();
    }
}