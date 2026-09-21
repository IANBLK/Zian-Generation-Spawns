package net.dan2026.cobblemongenerationspawns.common.server.spawns;

import com.cobblemon.mod.common.Cobblemon;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

/**
 * Lightweight diagnostics for Zian Generation Spawns.
 * Counters are observational only: they never decide whether a spawn is allowed.
 */
public final class SpawnStats {
    public enum Source {
        SPAWNABLE("filtro temprano"),
        WEIGHT("peso de spawn"),
        SNACK("Poke Snack"),
        FINAL_EVENT("barrera final");

        private final String label;
        Source(String label) { this.label = label; }
    }

    private static final int MAX_TRACKED_SPECIES = 50;
    private static final int COUNT = Source.values().length;
    private static final LongAdder[] ALLOWED = newCounters();
    private static final LongAdder[] BLOCKED = newCounters();
    private static final AtomicBoolean[] FIRST_SEEN = newFlags();
    private static final Set<String> UNLABELED = ConcurrentHashMap.newKeySet();
    private static final Set<String> UNKNOWN = ConcurrentHashMap.newKeySet();
    private static volatile String lastAllowed = "-";
    private static volatile String lastBlocked = "-";

    private SpawnStats() {}

    private static LongAdder[] newCounters() {
        LongAdder[] values = new LongAdder[COUNT];
        for (int i = 0; i < COUNT; i++) values[i] = new LongAdder();
        return values;
    }

    private static AtomicBoolean[] newFlags() {
        AtomicBoolean[] values = new AtomicBoolean[COUNT];
        for (int i = 0; i < COUNT; i++) values[i] = new AtomicBoolean();
        return values;
    }

    public static void record(Source source, boolean allowed, String species) {
        int index = source.ordinal();
        if (FIRST_SEEN[index].compareAndSet(false, true)) {
            Cobblemon.LOGGER.info("Zian Generation Spawns: primera evaluacion desde {}", source.label);
        }
        String name = species == null ? "(sin especie)" : species;
        if (allowed) {
            ALLOWED[index].increment();
            lastAllowed = name;
        } else {
            BLOCKED[index].increment();
            lastBlocked = name;
        }
    }

    public static void recordUnlabeled(String species) {
        if (species != null && UNLABELED.size() < MAX_TRACKED_SPECIES && UNLABELED.add(species)) {
            Cobblemon.LOGGER.warn("Zian Generation Spawns: especie sin etiqueta genN: {}", species);
        }
    }

    public static void recordUnknown(String species) {
        if (species != null && UNKNOWN.size() < MAX_TRACKED_SPECIES && UNKNOWN.add(species)) {
            Cobblemon.LOGGER.warn("Zian Generation Spawns: especie desconocida para Cobblemon, se bloquea: {}", species);
        }
    }

    public static void reset() {
        for (int i = 0; i < COUNT; i++) {
            ALLOWED[i].reset();
            BLOCKED[i].reset();
            FIRST_SEEN[i].set(false);
        }
        UNLABELED.clear();
        UNKNOWN.clear();
        lastAllowed = "-";
        lastBlocked = "-";
    }

    public static List<String> report() {
        List<String> lines = new ArrayList<>();
        for (Source source : Source.values()) {
            int index = source.ordinal();
            lines.add(source.label + ": permitidos=" + ALLOWED[index].sum() + ", bloqueados=" + BLOCKED[index].sum());
        }
        lines.add("Ultimo permitido: " + lastAllowed + " | ultimo bloqueado: " + lastBlocked);
        lines.add("Sin etiqueta genN: " + new TreeSet<>(UNLABELED));
        lines.add("Especies desconocidas: " + new TreeSet<>(UNKNOWN));
        return lines;
    }
}
