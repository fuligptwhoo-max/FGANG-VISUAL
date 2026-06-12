package com.fgangvisuals.util.commands.defaults;

import com.fgangvisuals.util.chat.ChatUtil;
import com.fgangvisuals.util.commands.api.Command;
import com.fgangvisuals.util.commands.api.argument.IArgConsumer;
import com.fgangvisuals.util.commands.api.exception.CommandException;
import com.fgangvisuals.util.neuro.rotation.AIRotationManager;

import java.util.List;
import java.util.stream.Stream;

public class AICommand extends Command {
    
    public AICommand() {
        super("ai");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        if (!args.hasAny()) {
            printHelp();
            return;
        }

        String subcommand = args.getString().toLowerCase();

        switch (subcommand) {
            case "save" -> {
                if (!args.hasAny()) {
                    ChatUtil.send("§cИспользование: §f.ai save <name>");
                    return;
                }
                String name = args.getString();
                AIRotationManager.saveDataset(name);
            }

            case "load" -> {
                if (!args.hasAny()) {
                    ChatUtil.send("§cИспользование: §f.ai load <modelname>");
                    return;
                }
                String modelName = args.getString();
                AIRotationManager.loadModel(modelName);
            }

            case "train" -> {
                if (!args.has(2)) {
                    ChatUtil.send("§cИспользование: §f.ai train <dataset> <modelname>");
                    return;
                }
                String datasetName = args.getString();
                String modelName = args.getString();
                
                ChatUtil.send("§7Начинаю обучение...");
                // Запускаем в отдельном потоке чтобы не блокировать игру
                new Thread(() -> {
                    AIRotationManager.trainModel(datasetName, modelName);
                }).start();
            }

            case "list" -> {
                AIRotationManager.listFiles();
            }

            case "dir" -> {
                AIRotationManager.openDirectory();
            }

            default -> {
                ChatUtil.send("§cНеизвестная подкоманда: §f" + subcommand);
                printHelp();
            }
        }
    }

    private void printHelp() {
        ChatUtil.send("§e§l=== AI Rotation Commands ===");
        ChatUtil.send("§f.ai save <name> §7- Сохранить датасет");
        ChatUtil.send("§f.ai train <dataset> <model> §7- Обучить модель");
        ChatUtil.send("§f.ai load <model> §7- Загрузить модель");
        ChatUtil.send("§f.ai list §7- Список файлов");
        ChatUtil.send("§f.ai dir §7- Открыть папку");
    }

    @Override
    public String getShortDesc() {
        return "Управление AI ротациями";
    }

    @Override
    public List<String> getLongDesc() {
        return List.of(
                "Команда для обучения и использования AI моделей ротаций",
                "",
                "Использование:",
                ".ai save <name> - сохранить датасет",
                ".ai train <dataset> <model> - обучить модель",
                ".ai load <model> - загрузить модель",
                ".ai list - список файлов",
                ".ai dir - открыть папку"
        );
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) {
        if (args.hasExactlyOne()) {
            return Stream.of("save", "load", "train", "list", "dir");
        }
        return Stream.empty();
    }
}
