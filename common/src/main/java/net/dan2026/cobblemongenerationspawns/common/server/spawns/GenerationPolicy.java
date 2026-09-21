/*
 *
 * Cobblemon: Generation Spawning - A NeoForge Minecraft Mod.
 *
 * Copyright (c) 2026 DAN2026. All rights reserved.
 *
 * This software is licensed under the CobblemonGenerationSpawning License v1.0.
 *  A copy of this License should have been included with this software.
 *  If not, you can obtain a copy at [https://github.com/DAN2026/CobblemonGenerationSpawning/blob/master/LICENSE].
 */

package net.dan2026.cobblemongenerationspawns.common.server.spawns;

import java.util.Set;

/** Explicit Cobblemon 1.8.1 labels: gen7b (Let's Go) and gen8a (Hisui). */
public final class GenerationPolicy {
    public static final Set<String> VALID_IDS = Set.of(
            "gen1", "gen2", "gen3", "gen4", "gen5", "gen6", "gen7", "gen8", "gen9");

    private GenerationPolicy() {}

    public static boolean allows(Set<String> labels, Set<String> enabled) {
        return labels.stream().anyMatch(label -> {
            String generation = switch (label) {
                case "gen7b" -> "gen7";
                case "gen8a" -> "gen8";
                default -> label;
            };
            return VALID_IDS.contains(generation) && enabled.contains(generation);
        });
    }
}
