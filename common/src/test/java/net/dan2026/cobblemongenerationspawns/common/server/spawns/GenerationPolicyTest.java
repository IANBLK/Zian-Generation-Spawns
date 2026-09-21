package net.dan2026.cobblemongenerationspawns.common.server.spawns;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class GenerationPolicyTest {
    @Test void emptyProgressionBlocksPokemon() {
        assertFalse(GenerationPolicy.allows(Set.of("gen1"), Set.of()));
    }
    @Test void eachGenerationRequiresItsOwnUnlock() {
        for (int species = 1; species <= 9; species++) {
            for (int enabled = 1; enabled <= 9; enabled++) {
                assertEquals(species == enabled, GenerationPolicy.allows(
                        Set.of("gen" + species), Set.of("gen" + enabled)));
            }
        }
    }
    @Test void letsGoBelongsToGenerationSeven() {
        assertTrue(GenerationPolicy.allows(Set.of("gen7b", "mythical"), Set.of("gen7")));
        assertFalse(GenerationPolicy.allows(Set.of("gen7b"), Set.of("gen8")));
    }
    @Test void hisuiBelongsToGenerationEight() {
        assertTrue(GenerationPolicy.allows(Set.of("gen8a"), Set.of("gen8")));
        assertFalse(GenerationPolicy.allows(Set.of("gen8a"), Set.of("gen7")));
    }
    @Test void similarPrefixesDoNotUnlockOtherGenerations() {
        assertFalse(GenerationPolicy.allows(Set.of("gen10"), Set.of("gen1")));
        assertFalse(GenerationPolicy.allows(Set.of("gen1custom"), Set.of("gen1")));
        assertFalse(GenerationPolicy.allows(Set.of("gen8custom"), Set.of("gen8")));
    }
    @Test void unknownOrMissingLabelsStayBlocked() {
        assertFalse(GenerationPolicy.allows(Set.of(), GenerationPolicy.VALID_IDS));
        assertFalse(GenerationPolicy.allows(Set.of("legendary"), GenerationPolicy.VALID_IDS));
    }
    @Test void invalidSavedIdsCannotGrantAccess() {
        assertFalse(GenerationPolicy.allows(Set.of("gen10"), Set.of("gen10")));
    }
    @Test void multipleUnlocksAreAnAllowlist() {
        assertTrue(GenerationPolicy.allows(Set.of("gen3", "starter"), Set.of("gen1", "gen3")));
        assertFalse(GenerationPolicy.allows(Set.of("gen2"), Set.of("gen1", "gen3")));
    }
}
