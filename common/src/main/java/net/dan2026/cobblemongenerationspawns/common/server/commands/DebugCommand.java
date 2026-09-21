package net.dan2026.cobblemongenerationspawns.common.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnFactors;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnStats;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.Set;

public final class DebugCommand {
    private DebugCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("generation")
                .then(Commands.literal("debug")
                        .requires(s -> s.hasPermission(2))
                        .executes(DebugCommand::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        ServerLevel overworld = context.getSource().getServer().overworld();
        Set<String> cachedGens = SpawnFactors.getCachedGenerations();
        Set<String> persistentGens = SpawnFactors.getPersistentGenerations(overworld);

        context.getSource().sendSuccess(() -> Component.literal("Cached Generations: [")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.join(", ", cachedGens)).withStyle(ChatFormatting.GREEN))
                .append(Component.literal("]").withStyle(ChatFormatting.WHITE)), false);

        context.getSource().sendSuccess(() -> Component.literal("Persistent Generations: [")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.join(", ", persistentGens)).withStyle(ChatFormatting.GREEN))
                .append(Component.literal("]").withStyle(ChatFormatting.WHITE)), false);

        for (String line : SpawnStats.report()) {
            context.getSource().sendSuccess(() -> Component.literal(line).withStyle(ChatFormatting.YELLOW), false);
        }
        return 1;
    }
}
