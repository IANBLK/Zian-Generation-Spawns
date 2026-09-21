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
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import net.dan2026.cobblemongenerationspawns.common.server.spawns.SpawnFactors;

public final class EventRegistry {
    private static ObservableSubscription<PokeSnackSpawnPokemonEvent.Pre> subscription;

    public static void register() {
        if (subscription != null) return;
        subscription = CobblemonEvents.POKE_SNACK_SPAWN_POKEMON_PRE.subscribe(Priority.NORMAL, event -> {
            var detail = event.getSpawnAction().getDetail();
            if (detail instanceof PokemonSpawnDetail && !SpawnFactors.matchesActiveGeneration(detail)) {
                event.cancel();
            }
        });
    }

    public static void unregister() {
        if (subscription != null) subscription.unsubscribe();
        subscription = null;
    }
}
