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

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail;
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence;
import com.cobblemon.mod.common.api.spawning.position.SpawnablePosition;
import com.cobblemon.mod.common.pokemon.Species;
import net.dan2026.cobblemongenerationspawns.common.server.data.GenerationData;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class SpawnFactors implements SpawningInfluence {
    // Readers always see a complete immutable snapshot, including during startup/reset.
    private static volatile Set<String> cachedGenerations = Set.of();

    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail detail, @NotNull SpawnablePosition position) {
        return !(detail instanceof PokemonSpawnDetail) || matchesActiveGeneration(detail);
    }

    @Override
    public float affectWeight(@NotNull SpawnDetail detail, @NotNull SpawnablePosition position, float weight) {
        return affectSpawnable(detail, position) ? weight : 0.0f;
    }

    public static boolean matchesActiveGeneration(SpawnDetail detail) {
        if (!(detail instanceof PokemonSpawnDetail pokemon)) return false;
        String name = pokemon.getPokemon().getSpecies();
        if (name == null) return false;
        Species species = PokemonSpecies.getByName(name);
        return matchesActiveGeneration(species);
    }

    /**
     * Final generation check for an already-created Pokemon entity.
     *
     * Cobblemon 1.8.x exposes a cancelable POKEMON_ENTITY_SPAWN event immediately
     * before BestSpawner adds the entity to the world.  Keeping this check in
     * addition to the SpawningInfluence makes generation locking fail closed even
     * if a spawner was created before our influence builder was registered.
     */
    public static boolean matchesActiveGeneration(Species species) {
        return species != null && GenerationPolicy.allows(species.getLabels(), cachedGenerations);
    }

    public static void addGeneration(ServerLevel level, String generation) {
        GenerationData.get(level).addPersistentGeneration(generation);
        updateCachedGenerations(level);
    }

    public static void removeGeneration(ServerLevel level, String generation) {
        GenerationData.get(level).removePersistentGeneration(generation);
        updateCachedGenerations(level);
    }

    public static Set<String> getPersistentGenerations(ServerLevel level) {
        return Set.copyOf(GenerationData.get(level).getPersistentGenerations());
    }

    public static void updateCachedGenerations(ServerLevel level) {
        cachedGenerations = getPersistentGenerations(level);
        Cobblemon.LOGGER.info("Zian Generation Spawns: generaciones globales activas: {}", cachedGenerations);
    }

    public static Set<String> getCachedGenerations() { return cachedGenerations; }
    public static void resetCache() { cachedGenerations = Set.of(); SpawnStats.reset(); }
}
