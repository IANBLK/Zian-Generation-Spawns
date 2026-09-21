package net.dan2026.cobblemongenerationspawns.common.server.registry;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.cooking.PokeSnackSpawnPokemonEvent;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnFactors;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnStats;

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

        pokemonSpawnSubscription = CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.HIGHEST, event -> {
            var species = event.getEntity().getPokemon().getSpecies();
            boolean allowed = SpawnFactors.matchesActiveGeneration(species);
            SpawnStats.record(SpawnStats.Source.FINAL_EVENT, allowed, species == null ? null : species.getName());
            if (!allowed) event.cancel();
        });
    }

    public static void unregister() {
        if (pokeSnackSubscription != null) pokeSnackSubscription.unsubscribe();
        if (pokemonSpawnSubscription != null) pokemonSpawnSubscription.unsubscribe();
        pokeSnackSubscription = null;
        pokemonSpawnSubscription = null;
    }
}
