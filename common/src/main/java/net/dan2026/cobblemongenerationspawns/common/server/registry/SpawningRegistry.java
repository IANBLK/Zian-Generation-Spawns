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

package net.dan2026.cobblemongenerationspawns.common.server.registry;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.spawning.BestSpawner;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory;
import kotlin.jvm.functions.Function1;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnFactors;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;

public final class SpawningRegistry {
    private static final SpawnFactors FACTOR = new SpawnFactors();
    private static final Function1<ServerPlayer, SpawningInfluence> BUILDER = player -> FACTOR;
    private static List<Function1<ServerPlayer, SpawningInfluence>> playerBuilders;
    private static List<SpawningInfluence> fishingInfluences;

    public static void register(MinecraftServer server) {
        if (playerBuilders != null) return;
        // Resolve both collections before changing either registration.
        var players = PlayerSpawnerFactory.INSTANCE.getInfluenceBuilders();
        var fishing = BestSpawner.INSTANCE.getFishingSpawner().getInfluences();
        players.add(BUILDER);
        try {
            fishing.add(FACTOR);
        } catch (RuntimeException error) {
            players.remove(BUILDER);
            throw error;
        }
        playerBuilders = players;
        fishingInfluences = fishing;
        Cobblemon.LOGGER.info("Zian Generation Spawns: filtros natural y pesca registrados");
    }

    public static void unregister() {
        if (playerBuilders != null) playerBuilders.remove(BUILDER);
        if (fishingInfluences != null) fishingInfluences.remove(FACTOR);
        playerBuilders = null;
        fishingInfluences = null;
    }
}
