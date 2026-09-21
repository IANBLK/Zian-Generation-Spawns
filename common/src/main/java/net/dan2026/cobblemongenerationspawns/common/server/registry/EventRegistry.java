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

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.cooking.PokeSnackSpawnPokemonEvent;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnFactors;

public final class EventRegistry {
    private static ObservableSubscription<PokeSnackSpawnPokemonEvent.Pre> pokeSnackSubscription;
    private static ObservableSubscription<SpawnEvent<PokemonEntity>> pokemonSpawnSubscription;

    public static void register() {
        if (pokeSnackSubscription != null || pokemonSpawnSubscription != null) return;

        pokeSnackSubscription = CobblemonEvents.POKE_SNACK_SPAWN_POKEMON_PRE.subscribe(Priority.NORMAL, event -> {
            var detail = event.getSpawnAction().getDetail();
            if (detail instanceof PokemonSpawnDetail && !SpawnFactors.matchesActiveGeneration(detail)) {
                event.cancel();
            }
        });

        // Defense in depth: this event is fired by BestSpawner immediately before
        // the Pokemon is inserted into the world. Cobblemon 1.8.x explicitly
        // honors cancellation here, so this catches natural/fishing/snack spawns
        // even if their spawner missed our earlier SpawningInfluence registration.
        pokemonSpawnSubscription = CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.HIGHEST, event -> {
            var species = event.getEntity().getPokemon().getSpecies();
            if (!SpawnFactors.matchesActiveGeneration(species)) {
                event.cancel();
            }
        });
    }

    public static void unregister() {
        if (pokeSnackSubscription != null) pokeSnackSubscription.unsubscribe();
        if (pokemonSpawnSubscription != null) pokemonSpawnSubscription.unsubscribe();
        pokeSnackSubscription = null;
        pokemonSpawnSubscription = null;
    }
}
