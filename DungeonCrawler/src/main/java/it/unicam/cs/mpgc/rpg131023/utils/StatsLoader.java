package it.unicam.cs.mpgc.rpg131023.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * Utility for loading configuration stats from JSON.
 * Isolates data parsing from the domain model.
 */
public final class StatsLoader {
    private static Map<String, CombatStats> statsCache = null;

    private StatsLoader() {
        // Prevents instantiation
    }

    /**
     * Lazily loads and returns the stats for a specific entity.
     * 
     * @param entityId The identifier of the entity (e.g. "hero", "goblin").
     * @return The stats associated with the entity.
     */
    public static CombatStats getStatsFor(final String entityId) {
        if (statsCache == null) {
            throw new IllegalStateException("StatsLoader has not been initialized. Call init() first.");
        }
        final CombatStats stats = statsCache.get(entityId.toLowerCase());
        if (stats == null) {
            throw new IllegalStateException("No stats found for entity: " + entityId);
        }
        return stats;
    }

    /**
     * Initializes the stats cache from a provided Reader.
     * 
     * @param reader The Reader containing JSON stats data.
     */
    public static void init(Reader reader) {
        try {
            final Gson gson = new Gson();
            final Type type = new TypeToken<Map<String, CombatStats>>() {
            }.getType();
            statsCache = gson.fromJson(reader, type);

            if (statsCache == null || statsCache.isEmpty()) {
                throw new IllegalStateException("Stats file is empty or malformed.");
            }
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new IllegalStateException("Critical error parsing stats JSON.", e);
        }
    }
}
