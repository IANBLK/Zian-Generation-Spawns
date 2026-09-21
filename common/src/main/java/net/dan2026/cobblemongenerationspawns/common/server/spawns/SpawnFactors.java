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
    private static volatile Set<String> cachedGenerations = Set.of();

    @Override
    public boolean affectSpawnable(@NotNull SpawnDetail detail, @NotNull SpawnablePosition position) {
        if (!(detail instanceof PokemonSpawnDetail)) return true;
        boolean allowed = matchesActiveGenerationInternal(detail);
        SpawnStats.record(SpawnStats.Source.SPAWNABLE, allowed, speciesName(detail));
        return allowed;
    }

    @Override
    public float affectWeight(@NotNull SpawnDetail detail, @NotNull SpawnablePosition position, float weight) {
        if (!(detail instanceof PokemonSpawnDetail)) return weight;
        boolean allowed = matchesActiveGenerationInternal(detail);
        SpawnStats.record(SpawnStats.Source.WEIGHT, allowed, speciesName(detail));
        return allowed ? weight : 0.0f;
    }

    /** Used by the Poke Snack PRE filter. */
    public static boolean matchesActiveGeneration(SpawnDetail detail) {
        boolean allowed = matchesActiveGenerationInternal(detail);
        SpawnStats.record(SpawnStats.Source.SNACK, allowed, speciesName(detail));
        return allowed;
    }

    private static boolean matchesActiveGenerationInternal(SpawnDetail detail) {
        if (!(detail instanceof PokemonSpawnDetail pokemon)) return false;
        String name = pokemon.getPokemon().getSpecies();
        if (name == null) return false;

        String lookupName = name;
        int colon = lookupName.indexOf(':');
        if (colon >= 0 && colon + 1 < lookupName.length()) {
            lookupName = lookupName.substring(colon + 1);
        }

        Species species;
        try {
            species = PokemonSpecies.getByName(lookupName);
        } catch (RuntimeException failure) {
            species = null;
        }
        if (species == null) SpawnStats.recordUnknown(name);
        return matchesActiveGeneration(species);
    }

    private static String speciesName(SpawnDetail detail) {
        if (!(detail instanceof PokemonSpawnDetail pokemon)) return null;
        return pokemon.getPokemon().getSpecies();
    }

    /**
     * Final generation check for an already-created Pokemon entity.
     * This keeps the proven alpha.2 behavior unchanged.
     */
    public static boolean matchesActiveGeneration(Species species) {
        if (species == null) return false;
        boolean hasGenerationLabel = species.getLabels().stream().anyMatch(label ->
                GenerationPolicy.VALID_IDS.contains(label) || "gen7b".equals(label) || "gen8a".equals(label));
        if (!hasGenerationLabel) SpawnStats.recordUnlabeled(species.getName());
        return GenerationPolicy.allows(species.getLabels(), cachedGenerations);
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

    public static void resetCache() {
        cachedGenerations = Set.of();
        SpawnStats.reset();
    }
}
